package com.project.cookEats.common_module.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileUploadController {

    @Autowired
    private FileUpLoadService fileUpLoadService;

    @PostMapping("/upload")
    public List<String> uploadFiles(@RequestParam("manualImage[]") MultipartFile[] files,
                                    @RequestParam("boardType") String boardType) {
        List<String> uploadedFiles = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                String fileUrl = fileUpLoadService.saveFile(file, boardType);
                uploadedFiles.add(fileUrl);
            } catch (Exception e) {
                e.printStackTrace();
                uploadedFiles.add("Failed to upload " + file.getOriginalFilename());
            }
        }
        return uploadedFiles;
    }
}
