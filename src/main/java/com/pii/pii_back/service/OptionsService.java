package com.pii.pii_back.service;

import com.pii.pii_back.entity.Options;
import com.pii.pii_back.repository.OptionsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OptionsService {

    private final OptionsRepository optionsRepository;

    public OptionsService(OptionsRepository optionsRepository) {
        this.optionsRepository = optionsRepository;
    }

    public Options create(Options options) {
        return optionsRepository.save(options);
    }

    public List<Options> getAll() {
        return optionsRepository.findAll();
    }

    public Options getById(Long id) {
        return optionsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Option non trouvee: " + id));
    }

    public Options update(Long id, Options options) {
        Options existing = getById(id);
        existing.setLibelle(options.getLibelle());
        existing.setUrl(options.getUrl());
        existing.setQuestion(options.getQuestion());
        return optionsRepository.save(existing);
    }

    public void delete(Long id) {
        if (!optionsRepository.existsById(id)) {
            throw new EntityNotFoundException("Option non trouvee: " + id);
        }
        optionsRepository.deleteById(id);
    }
}
