package com.pii.pii_back.controller;

import com.pii.pii_back.service.FileStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {
    private static final Logger logger = Logger.getLogger(FileUploadController.class.getName());

    private final FileStorageService fileStorageService;

    public FileUploadController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/image")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        logger.log(Level.INFO, "Upload image endpoint called");
        
        if (file == null) {
            logger.log(Level.WARNING, "File is null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Aucun fichier fourni"));
        }
        
        logger.log(Level.INFO, "File received: " + file.getOriginalFilename() + 
                              ", Size: " + file.getSize() + 
                              ", Type: " + file.getContentType());
        
        try {
            String filePath = fileStorageService.storeFile(file);
            logger.log(Level.INFO, "File stored successfully at: " + filePath);
            
            Map<String, String> response = new HashMap<>();
            response.put("filePath", filePath);
            response.put("message", "Image uploadée avec succès");
            
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IOException during upload: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erreur lors de l'upload : " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, "IllegalArgumentException during upload: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected exception during upload: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erreur inattendue : " + e.getMessage()));
        }
    }
}
