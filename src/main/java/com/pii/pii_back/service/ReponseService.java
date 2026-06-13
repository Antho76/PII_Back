package com.pii.pii_back.service;

import com.pii.pii_back.entity.Reponse;
import com.pii.pii_back.repository.ReponseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReponseService {

    private final ReponseRepository reponseRepository;

    public ReponseService(ReponseRepository reponseRepository) {
        this.reponseRepository = reponseRepository;
    }

    public Reponse create(Reponse reponse) {
        return reponseRepository.save(reponse);
    }

    public List<Reponse> getAll() {
        return reponseRepository.findAll();
    }

    public Reponse getById(Long id) {
        return reponseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reponse non trouvee: " + id));
    }

    public Reponse update(Long id, Reponse reponse) {
        Reponse existing = getById(id);
        existing.setReponses(reponse.getReponses());
        existing.setQuestion(reponse.getQuestion());
        return reponseRepository.save(existing);
    }

    public void delete(Long id) {
        if (!reponseRepository.existsById(id)) {
            throw new EntityNotFoundException("Reponse non trouvee: " + id);
        }
        reponseRepository.deleteById(id);
    }
}
