package com.pii.pii_back.service;

import com.pii.pii_back.entity.OptionReponse;
import com.pii.pii_back.repository.OptionReponseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OptionReponseService {

    private final OptionReponseRepository optionReponseRepository;

    public OptionReponseService(OptionReponseRepository optionReponseRepository) {
        this.optionReponseRepository = optionReponseRepository;
    }

    public OptionReponse create(OptionReponse optionReponse) {
        return optionReponseRepository.save(optionReponse);
    }

    public List<OptionReponse> getAll() {
        return optionReponseRepository.findAll();
    }

    public OptionReponse getById(Long id) {
        return optionReponseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("OptionReponse non trouvee: " + id));
    }

    public OptionReponse update(Long id, OptionReponse optionReponse) {
        OptionReponse existing = getById(id);
        existing.setRang(optionReponse.getRang());
        existing.setOption(optionReponse.getOption());
        existing.setReponse(optionReponse.getReponse());
        return optionReponseRepository.save(existing);
    }

    public void delete(Long id) {
        if (!optionReponseRepository.existsById(id)) {
            throw new EntityNotFoundException("OptionReponse non trouvee: " + id);
        }
        optionReponseRepository.deleteById(id);
    }
}
