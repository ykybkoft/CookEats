package com.project.cookEats.common_module.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileUploadController {

    @Autowired
    private FileUpLoadService fileUploadService;

    @PostMapping("/upload")
    public List<String> uploadFiles(@RequestParam("manualImage[]") MultipartFile[] files) {
        List<String> uploadedFiles = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                String fileName = fileUploadService.saveFile(file);
                uploadedFiles.add(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                uploadedFiles.add("Failed to upload " + file.getOriginalFilename());
            }
        }
        return uploadedFiles;
    }
}