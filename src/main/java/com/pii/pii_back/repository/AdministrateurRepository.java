package com.pii.pii_back.repository;

import com.pii.pii_back.entity.Administrateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdministrateurRepository extends JpaRepository<Administrateur, Long> {
	Optional<Administrateur> findByNomUtilisateurAndMotDePasse(String nomUtilisateur, String motDePasse);
}
