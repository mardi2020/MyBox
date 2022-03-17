package com.mardi2020.MyBox.file;


import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.mardi2020.MyBox.user.User;
import com.mardi2020.MyBox.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileRestController {

    private final FileService fileService;

    @RequestMapping(value = "/download/{fileId}", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadFromStorage(@PathVariable String fileId) {
        File targetFile = fileService.findFileById(fileId);

        String fileName = targetFile.getOriginalFileName();
        String editableFileName = targetFile.getFileName();
        String filePath = targetFile.getPath();
        Blob file = fileService.downloadFile("/" + filePath + "/" + fileName);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        file.downloadTo(os);
        byte[] filebytes = os.toByteArray();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+ editableFileName + "\"")
                .body(new ByteArrayResource(filebytes));
    }

    @PostMapping("/upload")
    public ResponseEntity uploadToStorage(@RequestParam String uploadFileName,
                                          @RequestParam MultipartFile uploadFile,
                                          @RequestParam(value = "filePath") String filePath,
                                          @RequestParam(value = "parentId") String parentId,
                                          HttpSession session) throws IOException {
        User user = (User) session.getAttribute("user");
        String email = user.getEmail();
        BlobInfo file = fileService.uploadFile(uploadFile, uploadFileName, filePath, parentId, email);
        return ResponseEntity.ok(file.toString());
    }

    @RequestMapping(value = "/{objectId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteFile(@PathVariable String objectId, HttpSession session) {
        ResponseEntity<String> responseEntity = null;

        try {
            File file = fileService.findFileById(objectId);
            String fileName = file.getOriginalFileName();
            String filePath = file.getPath();
            fileService.deleteFileFromDB(objectId);
            fileService.deleteObjectFromStorage(filePath + "/" + fileName);

            responseEntity = new ResponseEntity<>("DELETED " + fileName + " SUCCESS", HttpStatus.OK);

        } catch (Exception e){
            responseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity showFiles(HttpSession session,
                                    @RequestParam(defaultValue = "") String path) {
        ResponseEntity<FilesResponseDto> responseEntity = null;
        try {
            User targetUser = (User) session.getAttribute("user");
            String email = targetUser.getEmail();
            List<File> fileList = fileService.findFileIndir(email, path);

            FilesResponseDto fileResponse = new FilesResponseDto();
            fileResponse.setFiles(fileList);
            fileResponse.setMaxSize(targetUser.getMaxSize());
            fileResponse.setCurrentSize(targetUser.getCurrentSize());

            responseEntity = new ResponseEntity<>(fileResponse, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }

    @GetMapping(value = "/directory")
    public ResponseEntity directoryIdGET(@RequestParam(value = "path") String path,
                                         @RequestParam(value = "name") String name, HttpSession session) {
        ResponseEntity<String> responseEntity = null;

        try {
            String responseString = "";
            if (path.length() == 0) {
                // 루트 페이지 아이디 반환
                User targetUser = (User) session.getAttribute("user");
                responseString = fileService.getRootId(targetUser.getEmail());
            }
            else {
                File targetFile = fileService.findFileIdByPath(name, path);
                System.out.println("targetFile = " + targetFile);
                responseString = targetFile.getId();
            }
            responseEntity = new ResponseEntity<>(responseString, HttpStatus.OK);
        } catch (Exception e) {
            responseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @RequestMapping(value = "/directory", method = RequestMethod.POST)
    public ResponseEntity createDirectory(@RequestBody CreateDirResponseDto responseDto, HttpSession session) {
        ResponseEntity responseEntity = null;
        try {
            User targetUser = (User) session.getAttribute("user");
            String email = targetUser.getEmail();
            fileService.createFolder(responseDto.getPath(), responseDto.getDirectoryName(),
                    responseDto.getParentId(), email);

            responseEntity = new ResponseEntity<>("CREATE DIRECTORY SUCCESS", HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @RequestMapping(value = "/directory/{objectId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteDirectory(@PathVariable String objectId) {
        ResponseEntity<String> responseEntity = null;

        try {
            fileService.deleteFileFromDB(objectId);
            responseEntity = new ResponseEntity<>("DELETE " + objectId + " SUCCESS", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @RequestMapping(value = "/{objectId}", method = RequestMethod.PATCH)
    public ResponseEntity renameFileName(@PathVariable String objectId, @RequestBody FileUpdateDto updateDto) {
        ResponseEntity<String> responseEntity = null;
        try {
            System.out.println("updateDto = " + updateDto);
            String fileName = updateDto.getFileName();
            System.out.println("fileName = " + fileName);
            fileService.updateFileName(objectId, fileName);
            responseEntity = new ResponseEntity<>("UPDATE FILE NAME " + objectId + "SUCCESS", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }

//    // 유저간 파일 전송
//    @PostMapping("/share")
//    public ResponseEntity shareFileBetweenUser(@RequestBody FileShareDto fileShareDto) {
//        ResponseEntity<> responseEntity = null;
//        try {
//            fileService.
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//
//        return responseEntity;
//    }

}
