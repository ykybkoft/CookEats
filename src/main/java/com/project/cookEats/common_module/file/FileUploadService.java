package com.project.cookEats.common_module.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileUpLoadService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String saveFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Failed to store empty file.");
        }

        // Create the directory if it does not exist
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Resolve the file path
        Path path = Paths.get(uploadDir).resolve(file.getOriginalFilename());
        Files.copy(file.getInputStream(), path);

        // Return the filename or the path for future reference
        return file.getOriginalFilename();
    }
}