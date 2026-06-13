package com.pii.pii_back.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;
import java.util.logging.Level;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private static final Logger logger = Logger.getLogger(WebMvcConfig.class.getName());

    @Value("${file.upload-dir:/app/media}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Créer le chemin du répertoire d'upload
        Path uploadPath = Paths.get(uploadDir);
        
        // Créer le répertoire s'il n'existe pas
        try {
            Files.createDirectories(uploadPath);
            logger.log(Level.INFO, "Media directory ready: " + uploadPath.toAbsolutePath());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to create media directory: " + uploadPath, e);
        }
        
        // Configurer le gestionnaire de ressources statiques
        // Convertir le chemin en URI de fichier
        String fileUri = uploadPath.toUri().toString();
        if (!fileUri.endsWith("/")) {
            fileUri = fileUri + "/";
        }
        
        logger.log(Level.INFO, "Serving uploads from: " + fileUri);
        
        registry
                .addResourceHandler("/uploads/**")
                .addResourceLocations(fileUri);
    }
}
