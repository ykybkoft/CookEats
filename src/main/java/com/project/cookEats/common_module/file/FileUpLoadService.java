package com.project.cookEats.common_module.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FileUpLoadService {

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    private final AmazonS3 amazonS3;

    public String saveFile(MultipartFile file, String boardType) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("Failed to store empty file.");
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new RuntimeException("File size exceeds the limit of 5MB.");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new RuntimeException("File name cannot be null.");
        }

        // Extract the file extension and base name
        String extension = "";
        String baseName = originalFilename;

        if (originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            baseName = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        }

        // Generate unique file name using UUID
        String uniqueFileName = baseName + "_" + UUID.randomUUID() + extension;
        String keyName = boardType + "/" + uniqueFileName; // Key name in S3

        // Upload file to S3
        amazonS3.putObject(new PutObjectRequest(bucketName, keyName, file.getInputStream(), null));

        return amazonS3.getUrl(bucketName, keyName).toString(); // Return file URL
    }
}
