package com.pii.pii_back.config;

import com.pii.pii_back.entity.Administrateur;
import com.pii.pii_back.repository.AdministrateurRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public ApplicationRunner initializedDefaultAdmin(AdministrateurRepository administrateurRepository) {
        return args -> {
            // Vérifier si un admin par défaut existe déjà
            if (administrateurRepository.findByNomUtilisateurAndMotDePasse("admin", "admin").isEmpty()) {
                Administrateur adminDefault = new Administrateur();
                adminDefault.setNomUtilisateur("admin");
                adminDefault.setMotDePasse("admin");
                administrateurRepository.save(adminDefault);
                System.out.println("✓ Utilisateur admin par défaut créé (admin/admin)");
            } else {
                System.out.println("✓ Utilisateur admin par défaut existe déjà");
            }
        };
    }
}
