package com.project.cookEats.common_module.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;
import java.util.logging.Logger;

@RequiredArgsConstructor
@Service
public class FileUpLoadService {

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    private final AmazonS3 amazonS3;
    private static final Logger logger = Logger.getLogger(FileUpLoadService.class.getName());

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

        String extension = "";
        String baseName = originalFilename;

        if (originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            baseName = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        }

        String uniqueFileName = baseName + "_" + UUID.randomUUID() + extension;
        String keyName = boardType + "/" + uniqueFileName;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());

        amazonS3.putObject(new PutObjectRequest(bucketName, keyName, file.getInputStream(), metadata));

        logger.info("File uploaded successfully: " + keyName);
        return amazonS3.getUrl(bucketName, keyName).toString();
    }

    public void deleteFile(String fileUrl) {
        String fileKey = extractFileKey(fileUrl);

        try {
            if (amazonS3.doesObjectExist(bucketName, fileKey)) {
                amazonS3.deleteObject(new DeleteObjectRequest(bucketName, fileKey));
                logger.info("File deleted successfully: " + fileKey);
            } else {
                throw new RuntimeException("File not found: " + fileKey);
            }
        } catch (Exception e) {
            logger.severe("Error occurred while deleting the file from S3: " + e.getMessage());
            throw new RuntimeException("Error occurred while deleting the file from S3", e);
        }
    }

    private String extractFileKey(String fileUrl) {
        try {
            URI uri = new URI(fileUrl);
            String path = uri.getPath();
            return path.startsWith("/") ? path.substring(1) : path; // '/'를 제거하여 실제 키만 반환
        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid file URL: " + fileUrl, e);
        }
    }
}