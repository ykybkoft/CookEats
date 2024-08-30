package com.project.cookEats.common_module.file;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
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

        String extension = "";
        String baseName = originalFilename;

        if (originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            baseName = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        }

        String uniqueFileName = baseName + "_" + UUID.randomUUID() + extension;
        String keyName = boardType + "/" + uniqueFileName;

        amazonS3.putObject(new PutObjectRequest(bucketName, keyName, file.getInputStream(), null));

        return amazonS3.getUrl(bucketName, keyName).toString();
    }

    public void deleteFile(String fileUrl) {
        String fileKey = extractFileKey(fileUrl);

        try {
            if (amazonS3.doesObjectExist(bucketName, fileKey)) {
                amazonS3.deleteObject(new DeleteObjectRequest(bucketName, fileKey));
            } else {
                throw new RuntimeException("File not found: " + fileKey);
            }
        } catch (AmazonServiceException e) {
            throw new RuntimeException("Error occurred while deleting the file from S3", e);
        }
    }

    private String extractFileKey(String fileUrl) {
        try {
            java.net.URI uri = new java.net.URI(fileUrl);
            String path = uri.getPath();

            // 경로에서 첫 번째 '/'를 기준으로 키 추출
            return path.substring(1); // '/'를 제거하여 실제 키만 반환
        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid file URL: " + fileUrl, e);
        }
    }
}
