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
        System.out.println("fileName = " + fileName);
        FileUploadDto fileUploadDto = new FileUploadDto();
        fileUploadDto.setFileName(fileName);
        fileUploadDto.setFileSize(multipartFile.getSize());
        fileUploadDto.setPath(filePath);
        fileUploadDto.setOriginalFileName(fileName);

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

        return storage.create(
                BlobInfo.newBuilder("mybox_bucket", filePath + "/" + fileName)
                        .setAcl(new ArrayList<>(Collections.singletonList(Acl.of(Acl.User.ofAllAuthenticatedUsers(), Acl.Role.READER))))
                        .build(),
                multipartFile.getBytes()
        );
    }

    public void createFolder(String filePath, String folderName, String parentId) {
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
        folder.setUserId("testaccount123");
        folder.setPath(filePath);
        fileRepository.createFolder(folder);
    }

    public List<File> findFolderAll(String userId) {
        return fileRepository.findFolderAll(userId);
    }

    /**
     *
     * @param objectId 삭제할 타겟 파일
     */
    public void deleteFileFromDB(String objectId) {
        File targetFile = findFileById(objectId);
        long targetFileSize = targetFile.getFileSize();

        /* 삭제할 폴더의 id가 포함된 파일과 폴더도 같이 삭제 */
        List<File> files = fileRepository.findFileAll();
        for (File file : files) {
            for (String id : file.getParent()) {
                if(id.equals(objectId)) {
                    deleteFileFromDB(file.getId().toHexString());
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
}
