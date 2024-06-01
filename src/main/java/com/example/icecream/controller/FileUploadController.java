package com.example.icecream.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api")
public class FileUploadController {

    private static final String UPLOAD_DIR = "src/main/resources/static/images/";

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("image") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("No file selected");
        }

        try {
            // Ensure the upload directory exists
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Save the file locally
            Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
            Files.write(path, file.getBytes());

            // Return the URL of the uploaded file
            String imageUrl = "/images/" + file.getOriginalFilename();
            return ResponseEntity.ok().body(new UploadResponse(imageUrl));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to upload file");
        }
    }

    static class UploadResponse {
        public String imageUrl;

        public UploadResponse(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
}
