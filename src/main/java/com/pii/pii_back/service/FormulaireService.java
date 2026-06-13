package com.pii.pii_back.service;

import com.pii.pii_back.entity.Formulaire;
import com.pii.pii_back.repository.FormulaireRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormulaireService {

    private final FormulaireRepository formulaireRepository;

    public FormulaireService(FormulaireRepository formulaireRepository) {
        this.formulaireRepository = formulaireRepository;
    }

    public Formulaire create(Formulaire formulaire) {
        return formulaireRepository.save(formulaire);
    }

    public List<Formulaire> getAll() {
        return formulaireRepository.findAll();
    }

    public Formulaire getById(Long id) {
        return formulaireRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Formulaire non trouve: " + id));
    }

    public Formulaire update(Long id, Formulaire formulaire) {
        Formulaire existing = getById(id);
        existing.setLibelle(formulaire.getLibelle());
        existing.setAuteur(formulaire.getAuteur());
        return formulaireRepository.save(existing);
    }

    public void delete(Long id) {
        if (!formulaireRepository.existsById(id)) {
            throw new EntityNotFoundException("Formulaire non trouve: " + id);
        }
        formulaireRepository.deleteById(id);
    }
}
