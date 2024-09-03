package com.project.cookEats.common_module.file;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/files")
public class FileUploadController {

    private final FileUpLoadService fileUpLoadService;
    private static final Logger logger = Logger.getLogger(FileUploadController.class.getName());

    @PostMapping("/upload")
    public List<String> uploadFiles(@RequestParam("manualImage[]") MultipartFile[] files,
                                    @RequestParam("boardType") String boardType) {
        List<String> uploadedFiles = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                String fileUrl = fileUpLoadService.saveFile(file, boardType);
                uploadedFiles.add(fileUrl);
                logger.info("Uploaded file: " + file.getOriginalFilename());
            } catch (Exception e) {
                logger.severe("Failed to upload file: " + file.getOriginalFilename() + " - " + e.getMessage());
                uploadedFiles.add("Failed to upload " + file.getOriginalFilename() + ": " + e.getMessage());
            }
        }
        return uploadedFiles;
    }

    @PostMapping("/update")
    public List<String> updateFiles(@RequestParam("manualImage[]") MultipartFile[] newFiles,
                                    @RequestParam("existingFileUrls[]") List<String> existingFileUrls,
                                    @RequestParam("boardType") String boardType) {
        List<String> updatedFiles = new ArrayList<>(existingFileUrls);

        // 파일 이름 추출 및 매칭
        List<String> newFileNames = Arrays.stream(newFiles)
                .map(file -> getFilenameFromUrl(file.getOriginalFilename()))
                .collect(Collectors.toList());

        // 기존 파일 중 삭제된 파일을 처리
        for (String existingFileUrl : existingFileUrls) {
            String existingFileName = getFilenameFromUrl(existingFileUrl);
            boolean isFilePresent = newFileNames.contains(existingFileName);
            if (!isFilePresent) {
                fileUpLoadService.deleteFile(existingFileUrl);
                updatedFiles.remove(existingFileUrl);
            }
        }

        // 새로 추가된 파일들을 S3에 업로드
        for (MultipartFile newFile : newFiles) {
            try {
                String fileUrl = fileUpLoadService.saveFile(newFile, boardType);
                updatedFiles.add(fileUrl);
                logger.info("Uploaded file: " + newFile.getOriginalFilename());
            } catch (Exception e) {
                logger.severe("Failed to upload file: " + newFile.getOriginalFilename() + " - " + e.getMessage());
                updatedFiles.add("Failed to upload " + newFile.getOriginalFilename() + ": " + e.getMessage());
            }
        }

        return updatedFiles;
    }

    private String getFilenameFromUrl(String fileUrl) {
        // URL에서 파일명만 추출
        return fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
    }
}
