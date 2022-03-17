package com.mardi2020.MyBox.file;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.mardi2020.MyBox.user.User;
import com.mardi2020.MyBox.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.List;



@Controller
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    private final UserService userService;

    @RequestMapping(value = "/download/{fileId}", method = RequestMethod.GET)
    public ResponseEntity<Resource>  downloadFromStorage(@PathVariable String fileId) {
        File targetFile = fileService.findFileById(fileId);

        String fileName = targetFile.getOriginalFileName();
        System.out.println("fileName = " + fileName);
        String editableFileName = targetFile.getFileName();
        String filePath = targetFile.getPath();
        System.out.println("filePath = " + filePath);
        Blob file = fileService.downloadFile(filePath + "/" + fileName);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        file.downloadTo(os);
        byte[] filebytes = os.toByteArray();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+ editableFileName + "\"")
                .body(new ByteArrayResource(filebytes));
    }

    /**
     *
     * @param uploadFileName 업로드할 파일의 이름
     * @param uploadFile 파일
     * @return 응답
     * @throws IOException
     */
    @PostMapping("/upload")
    public ResponseEntity uploadToStorage(@RequestParam String uploadFileName,
                                          @RequestParam MultipartFile uploadFile,
                                          @RequestParam(value = "filePath") String filePath,
                                          @RequestParam(value = "parentId") String parentId,
                                          Principal principal) throws IOException {
        String email = principal.getName();
        BlobInfo file = fileService.uploadFile(uploadFile, uploadFileName, filePath, parentId, email);
        return ResponseEntity.ok(file.toString());
    }

    @PostMapping("/delete/{objectId}")
    public String deleteFileFromStorage(@PathVariable String objectId) {
        File file = fileService.findFileById(objectId);
        String fileName = file.getOriginalFileName();
        String filePath = file.getPath();
        fileService.deleteFileFromDB(objectId);
        fileService.deleteObjectFromStorage(filePath + "/" + fileName);

        return "redirect:/files";
    }

    @GetMapping("/delete/{objectId}")
    public String deleteFilePage(@PathVariable String objectId) {
        return "redirect:/";
    }

    @PostMapping("/createFolder")
    public ResponseEntity createFolder(@RequestParam(value = "filePath") String filePath,
                                       @RequestParam(value = "folder") String folderName,
                                       @RequestParam(value = "parentId") String parentId,
                                       Principal principal) {
        String email = principal.getName();
        fileService.createFolder(filePath, folderName, parentId, email);

        return ResponseEntity.ok("created "+ folderName);
    }

    /**
     * 각 경로에 맞는 파일, 폴더가 보여져야 함
     * @param model 모델
     * @return view page
     */
    @GetMapping("/files")
    public String FileListPage(@RequestParam(defaultValue = "") String path, Model model, Principal principal) {
        try {
            String email = principal.getName();
            User user = userService.getUserByEmail(email);
            Long userCurrentSize = user.getCurrentSize();
            Long userMaxSize = user.getMaxSize() / (1024 * 1024);
//            List<File> fileList = fileService.findFileAllByUserId(email);
            List<File> fileList = fileService.findFileIndir(email, path);

            model.addAttribute("MaxSize", userMaxSize);
            model.addAttribute("CurrentSize", userCurrentSize);
            model.addAttribute("fileList", fileList);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/file/fileList";
    }

    @GetMapping("/deleteFolder/{objectId}")
    public String deleteFolderPage(@PathVariable String objectId) {
        return "redirect:/files";
    }

    @PostMapping("/deleteDirectory/{objectId}")
    public String deleteDirectory(@PathVariable String objectId) {
        fileService.deleteFileFromDB(objectId);

        return "redirect:/files";
    }

    @PostMapping("/updateFileName/{objectId}")
    public String updateFileName(@PathVariable String objectId,
                                 FileUpdateDto updateFile) {
        String fileName = updateFile.getFileName();
        fileService.updateFileName(objectId, fileName);
        return "redirect:/files";
    }

    @GetMapping("/updateFileName/{objectId}")
    public String updateFileNamePage(@PathVariable String objectId, Model model) {
        model.addAttribute("updateFile", new FileUpdateDto());
        return "redirect:/files";
    }

    @PostMapping("/files/{filePath}")
    public boolean filePathPost(@PathVariable String filePath) {
        return true;
    }


    @GetMapping("/updateFilePath/{objectId}")
    public void updateFilePathGet(@PathVariable String objectId) {

    }
}
