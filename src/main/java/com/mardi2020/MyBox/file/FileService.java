package com.mardi2020.MyBox.file;

import com.google.cloud.storage.*;
import com.mardi2020.MyBox.user.User;
import com.mardi2020.MyBox.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    private final Storage storage;

    private final UserRepository userRepository;

    public List<File> findFileAllByUserId(String userId) {
        return fileRepository.findFileAllByUserId(userId);
    }

    public Blob downloadFile(String downloadFileName) {

        return storage.get("mybox_bucket", downloadFileName);
    }

    /**
     *
     * @param multipartFile 업로드할 파일
     * @param fileName 파일이름
     * @param filePath 파일 경로 지정
     * @param parentId 가장 가까운 상위 폴더의 objectId
     * @return store in gcs
     * @throws IOException
     */
    public BlobInfo uploadFile(MultipartFile multipartFile, String fileName, String filePath, String parentId, String email) throws IOException {
        /* 이미 그 경로에 같은 이름의 파일이 있는지 검사*/
        String duplicateFileId = fileRepository.findDuplicateFile(fileName, filePath);
        if (duplicateFileId != null) {
            Random random = new Random();
            fileName = random.nextInt() + fileName;
        }

        FileUploadDto fileUploadDto = new FileUploadDto();
        fileUploadDto.setFileName(fileName);
        fileUploadDto.setFileSize(multipartFile.getSize());
        fileUploadDto.setOriginalFileName(fileName);

        StringBuilder newFilePath = new StringBuilder();

        if(filePath.length() > 0 && filePath.charAt(0) == '/') {
            for (int i = 1; i < filePath.length(); i++) {
                newFilePath.append(filePath.charAt(i));
            }
            fileUploadDto.setPath(newFilePath.toString());
        }
        else {
            fileUploadDto.setPath(filePath);
        }

        String[] nameAndExtension = fileName.split("\\.");
        if (nameAndExtension[0].length() > 7) {
            nameAndExtension[0] = nameAndExtension[0].substring(0, 7);
            nameAndExtension[0] += "...";
        }
        fileUploadDto.setShowFileName(nameAndExtension[0]);
        fileUploadDto.setExtension(nameAndExtension[1]);

        SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd HH:mm");
        Date time = new Date();
        String today = format.format(time);
        fileUploadDto.setCreatedDate(today);

        /* filePath에 포함된 모든 디렉토리의 objectId를 리스트에 삽입 */
        File file = findFileById(parentId);
        fileUploadDto.getParent().add(parentId);
        for (String s : file.getParent()) {
            if(!s.equals(parentId) && file.isDirectory()) {
                fileUploadDto.getParent().add(s);
            }
        }

        /* 부모 디렉토리에 현재 파일의 크기를 추가 */
        for (String parentObjectId : fileUploadDto.getParent()) {
            File p = findFileById(parentObjectId);
            fileRepository.updateFileSize(parentObjectId, p.getFileSize() + multipartFile.getSize());
        }
        fileUploadDto.setUserId(email);
        fileRepository.uploadFileToStorage(fileUploadDto);

        /* 유저의 사용 용량 증가 */
        User user = userRepository.getUserByEmail(email);
        Long userCurSize = user.getCurrentSize();
        userRepository.updateUserCurrentSize(email, userCurSize + multipartFile.getSize());

        /* 바로 위 부모 디렉토리에 현재 추가된 파일의 id 추가 */
        List<String> children = file.getChildren();

        if(filePath.charAt(0) == '/'){
            File target = fileRepository.findFileByPathAndName(newFilePath.toString(), fileName);
            children.add(target.getId());
        }
        else{
            File target = fileRepository.findFileByPathAndName(filePath, fileName);
            children.add(target.getId());
        }

        fileRepository.updateNearestParentChildList(parentId, children);

        return storage.create(
                BlobInfo.newBuilder("mybox_bucket", filePath + "/" + fileName)
                        .setAcl(new ArrayList<>(Collections.singletonList(Acl.of(Acl.User.ofAllAuthenticatedUsers(), Acl.Role.READER))))
                        .build(),
                multipartFile.getBytes()
        );
    }

    public void moveFile(String filePath, String fileId) {
        // 해당 파일의 정보 가져오기
        File targetFile = fileRepository.getFileById(fileId);

        // 파일의 경로를 새로운 경로로 바꾸기
        targetFile.setPath(filePath);

        fileRepository.updateFilePathByFileId(filePath, fileId);
    }

    public void createFolder(String filePath, String folderName, String parentId, String email) {
        FileUploadDto folder = new FileUploadDto();
        SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd HH:mm");
        Date time = new Date();
        String today = format.format(time);
        folder.setCreatedDate(today);
        folder.setFileName(folderName);
        folder.setOriginalFileName(folderName);

        File parent = findFileById(parentId);
        folder.getParent().add(parentId);
        for (String s : parent.getParent()) {
            if(!s.equals(parentId) && parent.isDirectory()) {
                folder.getParent().add(s);
            }
        }

        folder.setDirectory(true);
        folder.setUserId(email);
        StringBuilder newFilePath = new StringBuilder();

        if(filePath.length() > 0 && filePath.charAt(0) == '/') {
            for (int i = 1; i < filePath.length(); i++) {
                newFilePath.append(filePath.charAt(i));
            }
            System.out.println("newFilePath = " + newFilePath);
            folder.setPath(newFilePath.toString());
        }
        else {
            folder.setPath(filePath);
        }

        fileRepository.createFolder(folder);

        /* 현재 추가된 폴더를 상위 폴더의 children에 넣어줌 */
        List<String> children = parent.getChildren();
        if(filePath.charAt(0) == '/'){
            File target = fileRepository.findFileByPathAndName(newFilePath.toString(), folderName);
            children.add(target.getId());
        }
        else{
            File target = fileRepository.findFileByPathAndName(filePath, folderName);
            children.add(target.getId());
        }

        fileRepository.updateNearestParentChildList(parentId, children);

    }

    public String findFolderAll(String userId, String path) {
        File file = fileRepository.findPathId(userId, path);
        System.out.println("Find file = " + file);
        return file.getId();
    }

    /**
     *
     * @param objectId 삭제할 타겟 파일
     */
    public void deleteFileFromDB(String objectId) {
        File targetFile = findFileById(objectId);
        Long targetFileSize = targetFile.getFileSize();
        /* 유저의 사용 용량에서 타겟 파일의 크기만큼 빼주기 */
        String email = targetFile.getUserId();
        User user = userRepository.getUserByEmail(email);
        Long curSize = user.getCurrentSize();
        userRepository.subtractFileSize(curSize - targetFileSize, email);

        /* 삭제할 폴더의 id가 포함된 파일과 폴더도 같이 삭제 */
        List<File> files = fileRepository.findFileAll();
        for (File file : files) {
            for (String id : file.getParent()) {
                if(id.equals(objectId)) {
                    deleteFileFromDB(file.getId());
                    break;
                }
            }
        }
        /* 상위 폴더의 크기 수정 */
        for (String id : targetFile.getParent()) {
            File fileById = findFileById(id);
            fileRepository.updateFileSize(id, fileById.getFileSize() - targetFileSize);
        }

        fileRepository.deleteFile(objectId);
    }

    public File findFileById(String objectId) {
        return fileRepository.getFileById(objectId);
    }

    public void deleteObjectFromStorage(String fileName) {
        storage.delete("mybox_bucket", fileName);
    }

    public void updateFileName(String id, String fileName) {
        fileRepository.updateFileNameInDB(id, fileName);
    }


    public List<File> findFileIndir(String userId, String filePath){
        return fileRepository.findFileInDir(userId, filePath);
    }


    public File findFileIdByPath(String name, String path) {
        return fileRepository.findFileByPathAndName(path, name);
    }

    public String getRootId(String email) {
        File file = fileRepository.getRootId2(email);
        return file.getId();
    }
}
