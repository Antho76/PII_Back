package com.pii.pii_back.service;

import com.pii.pii_back.entity.Question;
import com.pii.pii_back.repository.QuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public Question create(Question question) {
        return questionRepository.save(question);
    }

    public List<Question> getAll() {
        return questionRepository.findAll();
    }

    public Question getById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Question non trouvee: " + id));
    }

    public Question update(Long id, Question question) {
        Question existing = getById(id);
        existing.setQuestion(question.getQuestion());
        existing.setType(question.getType());
        existing.setMediaImage(question.getMediaImage());
        existing.setFormulaire(question.getFormulaire());
        return questionRepository.save(existing);
    }

    public void delete(Long id) {
        if (!questionRepository.existsById(id)) {
            throw new EntityNotFoundException("Question non trouvee: " + id);
        }
        questionRepository.deleteById(id);
    }
}
