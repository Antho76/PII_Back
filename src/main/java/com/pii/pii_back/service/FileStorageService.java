package com.pii.pii_back.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.logging.Level;

@Service
public class FileStorageService {
    private static final Logger logger = Logger.getLogger(FileStorageService.class.getName());

    @Value("${file.upload-dir:/app/media}")
    private String uploadDir;

    public String storeFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Le fichier ne peut pas être vide");
        }

        // Vérifier le type de fichier
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Seules les images sont acceptées");
        }

        // Créer le chemin absolu du répertoire d'upload
        Path uploadPath = Paths.get(uploadDir);
        
        // Créer le répertoire s'il n'existe pas
        try {
            Files.createDirectories(uploadPath);
            logger.log(Level.INFO, "Upload directory ready: " + uploadPath.toAbsolutePath());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to create upload directory: " + uploadPath, e);
            throw new IOException("Impossible de créer le répertoire d'upload", e);
        }

        // Générer un nom de fichier unique
        String fileExtension = getFileExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID().toString() + fileExtension;

        // Sauvegarder le fichier
        Path filePath = uploadPath.resolve(fileName);
        try {
            Files.write(filePath, file.getBytes());
            logger.log(Level.INFO, "File stored successfully: " + filePath.toAbsolutePath() + " (size: " + file.getSize() + " bytes)");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to store file: " + filePath, e);
            throw new IOException("Impossible de sauvegarder le fichier", e);
        }

        // Retourner le chemin relatif
        return "/uploads/" + fileName;
    }

    private String getFileExtension(String fileName) {
        if (fileName == null) {
            return ".jpg";
        }
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot > 0) {
            return fileName.substring(lastDot).toLowerCase();
        }
        return ".jpg";
    }
}
