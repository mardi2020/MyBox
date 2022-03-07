package com.mardi2020.MyBox.file;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
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
import java.util.List;



@Controller
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @RequestMapping(value = "/download/{fileId}", method = RequestMethod.GET)
    public ResponseEntity<Resource>  downloadFromStorage(@PathVariable String fileId) {
        File targetFile = fileService.findFileById(fileId);
        System.out.println("targetFile = " + targetFile);
        String fileName = targetFile.getOriginalFileName();
        String editableFileName = targetFile.getFileName();
        String filePath = targetFile.getPath();
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
                                          @RequestParam(value = "parentId") String parentId) throws IOException {
        BlobInfo file = fileService.uploadFile(uploadFile, uploadFileName, filePath, parentId);

        return ResponseEntity.ok(file.toString());
    }

    @PostMapping("/delete/{objectId}")
    public String deleteFileFromStorage(@PathVariable String objectId) {
        File file = fileService.findFileById(objectId);
        String fileName = file.getOriginalFileName();
        String filePath = file.getPath();
        fileService.deleteFileFromDB(objectId);
        fileService.deleteObjectFromStorage(filePath + "/" + fileName);

        return "redirect:/";
    }

    @GetMapping("/delete/{objectId}")
    public String deleteFilePage(@PathVariable String objectId) {
        return "redirect:/";
    }

    @PostMapping("/createFolder")
    public ResponseEntity createFolder(@RequestParam(value = "filePath") String filePath,
                                       @RequestParam(value = "folder") String folderName,
                                       @RequestParam(value = "parentId") String parentId) {

        fileService.createFolder(filePath, folderName, parentId);

        return ResponseEntity.ok("created "+ folderName);
    }

    /**
     * 각 경로에 맞는 파일, 폴더가 보여져야 함
     * @param model 모델
     * @return view page
     */
    @GetMapping("/")
    public String FileListPage(Model model) {
        List<File> fileList = fileService.findFileAllByUserId("testaccount123");
        List<File> folderList = fileService.findFolderAll();

        model.addAttribute("fileList", fileList);
        model.addAttribute("folderList", folderList);

        return "/file/fileList";
    }

    @GetMapping("/deleteFolder/{objectId}")
    public String deleteFolderPage(@PathVariable String objectId) {
        return "redirect:/";
    }

    @PostMapping("/deleteDirectory/{objectId}")
    public String deleteDirectory(@PathVariable String objectId) {
        fileService.deleteFileFromDB(objectId);

        return "redirect:/";
    }

    @PostMapping("/updateFileName/{objectId}")
    public String updateFileName(@PathVariable String objectId,
                                 FileUpdateDto updateFile) {
        String fileName = updateFile.getFileName();
        fileService.updateFileName(objectId, fileName);
        return "redirect:/";
    }

    @GetMapping("/updateFileName/{objectId}")
    public String updateFileNamePage(@PathVariable String objectId, Model model) {
        model.addAttribute("updateFile", new FileUpdateDto());
        return "redirect:/";
    }
}
