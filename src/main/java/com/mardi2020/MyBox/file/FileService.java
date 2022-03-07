package com.mardi2020.MyBox.file;

import com.google.cloud.storage.*;
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

    public List<File> findFileAll() {
        return fileRepository.findFileAll();
    }

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
    public BlobInfo uploadFile(MultipartFile multipartFile, String fileName, String filePath, String parentId) throws IOException {
        FileUploadDto fileUploadDto = new FileUploadDto();
        fileUploadDto.setFileName(fileName);
        fileUploadDto.setFileSize(multipartFile.getSize());
        fileUploadDto.setPath(filePath);

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
        /* 로그인 기능 구현하면 변경 */
        fileUploadDto.setUserId("testaccount123");
        fileRepository.uploadFileToStorage(fileUploadDto);

        return storage.create(
                BlobInfo.newBuilder("mybox_bucket", fileName)
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

    public List<File> findFolderAll() {
        return fileRepository.findFolderAll();
    }

    public void deleteFileFromDB(String objectId) {
        File targetFile = findFileById(objectId);
        long targetFileSize = targetFile.getFileSize();

        List<File> files = findFileAll();
        for (File file : files) {
            for (String id : file.getParent()) {
                if(id.equals(objectId)) {
                    deleteFileFromDB(file.getId().toHexString());
                    break;
                }
            }
        }

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

}
