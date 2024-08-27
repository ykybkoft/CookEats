package com.project.cookEats.common_module.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/files")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = fileUploadService.saveFile(file);
            return "File uploaded successfully: " + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return "File upload failed";
        }
    }
}
