package com.pii.pii_back.service;

import com.pii.pii_back.entity.Administrateur;
import com.pii.pii_back.repository.AdministrateurRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdministrateurService {

    private final AdministrateurRepository administrateurRepository;

    public AdministrateurService(AdministrateurRepository administrateurRepository) {
        this.administrateurRepository = administrateurRepository;
    }

    public Administrateur create(Administrateur administrateur) {
        return administrateurRepository.save(administrateur);
    }

    public List<Administrateur> getAll() {
        return administrateurRepository.findAll();
    }

    public Administrateur getById(Long id) {
        return administrateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Administrateur non trouve: " + id));
    }

    public Administrateur update(Long id, Administrateur administrateur) {
        Administrateur existing = getById(id);
        existing.setNomUtilisateur(administrateur.getNomUtilisateur());
        existing.setMotDePasse(administrateur.getMotDePasse());
        return administrateurRepository.save(existing);
    }

    public void delete(Long id) {
        if (!administrateurRepository.existsById(id)) {
            throw new EntityNotFoundException("Administrateur non trouve: " + id);
        }
        administrateurRepository.deleteById(id);
    }
}
