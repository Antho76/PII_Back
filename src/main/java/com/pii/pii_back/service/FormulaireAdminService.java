package com.pii.pii_back.service;

import com.pii.pii_back.dto.request.FormulaireCompletRequestDto;
import com.pii.pii_back.dto.request.SubmitResponsesRequestDto;
import com.pii.pii_back.dto.response.FormulaireCompletResponseDto;
import com.pii.pii_back.dto.response.FormulaireListItemResponseDto;
import com.pii.pii_back.dto.response.FormulaireReponseResponseDto;
import com.pii.pii_back.entity.Administrateur;
import com.pii.pii_back.entity.Formulaire;
import com.pii.pii_back.entity.Options;
import com.pii.pii_back.entity.Question;
import com.pii.pii_back.entity.Reponse;
import com.pii.pii_back.entity.enums.QuestionType;
import com.pii.pii_back.repository.AdministrateurRepository;
import com.pii.pii_back.repository.FormulaireRepository;
import com.pii.pii_back.repository.ReponseRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FormulaireAdminService {

    private final FormulaireRepository formulaireRepository;
    private final AdministrateurRepository administrateurRepository;
    private final ReponseRepository reponseRepository;

    public FormulaireAdminService(
            FormulaireRepository formulaireRepository,
            AdministrateurRepository administrateurRepository,
            ReponseRepository reponseRepository
    ) {
        this.formulaireRepository = formulaireRepository;
        this.administrateurRepository = administrateurRepository;
        this.reponseRepository = reponseRepository;
    }

    @Transactional
    public FormulaireCompletResponseDto createFormulaireComplet(FormulaireCompletRequestDto requestDto) {
        Administrateur auteur = administrateurRepository.findById(requestDto.auteurId())
                .orElseThrow(() -> new EntityNotFoundException("Administrateur non trouve: " + requestDto.auteurId()));

        Formulaire formulaire = new Formulaire();
        formulaire.setLibelle(requestDto.libelle());
        formulaire.setDescription(requestDto.description());
        if (requestDto.etat() != null) {
            try {
                formulaire.setEtat(com.pii.pii_back.entity.enums.FormulaireEtat.valueOf(requestDto.etat()));
            } catch (IllegalArgumentException e) {
                // ignore invalid state and keep default
            }
        }
        ensureShareToken(formulaire);
        formulaire.setAuteur(auteur);

        List<Question> questions = new ArrayList<>();
        int questionIndex = 0;
        for (FormulaireCompletRequestDto.QuestionCreationDto questionDto : requestDto.questions()) {
            Question question = new Question();
            question.setQuestion(questionDto.question());
            question.setDescription(questionDto.description());
            question.setType(questionDto.type());
            question.setPositionIndex(questionDto.positionIndex() != null ? questionDto.positionIndex() : questionIndex++);
            
            // Set default min/max values for sliders if not provided
            if (questionDto.type() == QuestionType.SPIDER_INT) {
                question.setMinValue(questionDto.minValue() != null ? questionDto.minValue() : 0);
                question.setMaxValue(questionDto.maxValue() != null ? questionDto.maxValue() : 5);
            } else {
                question.setMinValue(questionDto.minValue());
                question.setMaxValue(questionDto.maxValue());
            }
            
            question.setMediaImage(questionDto.mediaImage());
            question.setFormulaire(formulaire);

            List<FormulaireCompletRequestDto.OptionCreationDto> requestOptions = questionDto.options() == null
                    ? List.of()
                    : questionDto.options();

            if (requiresOptions(questionDto.type()) && requestOptions.isEmpty()) {
                throw new IllegalArgumentException("La question de type " + questionDto.type() + " doit contenir des options.");
            }

            List<Options> options = new ArrayList<>();
            for (FormulaireCompletRequestDto.OptionCreationDto optionDto : requestOptions) {
                Options option = new Options();
                option.setLibelle(optionDto.libelle());
                option.setUrl(optionDto.url());
                option.setQuestion(question);
                options.add(option);
            }

            question.setOptions(options);
            questions.add(question);
        }

        formulaire.setQuestions(questions);
        Formulaire saved = formulaireRepository.save(formulaire);
        return toResponse(saved);
    }

        @Transactional
        public List<FormulaireListItemResponseDto> getFormulaires() {
        return formulaireRepository.findAll().stream()
            .peek(this::ensureShareToken)
            .map(formulaire -> new FormulaireListItemResponseDto(
                formulaire.getId(),
                formulaire.getLibelle(),
                formulaire.getDescription(),
                formulaire.getAuteur().getId(),
                formulaire.getAuteur().getNomUtilisateur(),
                    formulaire.getQuestions() == null ? 0 : formulaire.getQuestions().size(),
                    reponseRepository.countByFormulaireId(formulaire.getId()),
                formulaire.getEtat() == null ? com.pii.pii_back.entity.enums.FormulaireEtat.PENDING.name() : formulaire.getEtat().name(),
                formulaire.getShareToken()
            ))
            .toList();
        }

        @Transactional
        public FormulaireCompletResponseDto getFormulaireById(Long formulaireId) {
        Formulaire formulaire = formulaireRepository.findById(formulaireId)
            .orElseThrow(() -> new EntityNotFoundException("Formulaire non trouve: " + formulaireId));
        ensureShareToken(formulaire);
        return toResponse(formulaire);
        }

        @Transactional
        public FormulaireReponseResponseDto getFormulaireResponses(Long formulaireId) {
        Formulaire formulaire = formulaireRepository.findById(formulaireId)
            .orElseThrow(() -> new EntityNotFoundException("Formulaire non trouve: " + formulaireId));

        List<Reponse> responses = reponseRepository.findByQuestion_Formulaire_Id(formulaireId);

        List<FormulaireReponseResponseDto.ResponseItemDto> responseItems = responses.stream()
            .map(response -> new FormulaireReponseResponseDto.ResponseItemDto(
                response.getId(),
                response.getResponseId(),
                response.getQuestion().getId(),
                response.getQuestion().getQuestion(),
                response.getQuestion().getType(),
                response.getQuestion().getMediaImage(),
                response.getReponses()
            ))
            .toList();

        return new FormulaireReponseResponseDto(
            formulaire.getId(),
            formulaire.getLibelle(),
            responses.size(),
            responseItems
        );
    }
    private boolean requiresOptions(QuestionType type) {
        return type == QuestionType.MULTIPLE_CHOICE
                || type == QuestionType.RADIO_BUTTON
                || type == QuestionType.IMAGE_RANKING;
    }

    private FormulaireCompletResponseDto toResponse(Formulaire formulaire) {
        List<FormulaireCompletResponseDto.QuestionDetailDto> questionDtos = formulaire.getQuestions().stream()
            .sorted((left, right) -> {
                int leftIndex = left.getPositionIndex() == null ? Integer.MAX_VALUE : left.getPositionIndex();
                int rightIndex = right.getPositionIndex() == null ? Integer.MAX_VALUE : right.getPositionIndex();
                return Integer.compare(leftIndex, rightIndex);
            })
            .map(question -> new FormulaireCompletResponseDto.QuestionDetailDto(
                        question.getId(),
                        question.getQuestion(),
                        question.getDescription(),
                        question.getType(),
                        question.getMinValue(),
                        question.getMaxValue(),
                        question.getMediaImage(),
                question.getPositionIndex(),
                        question.getOptions().stream()
                                .map(option -> new FormulaireCompletResponseDto.OptionDetailDto(
                                        option.getId(),
                                        option.getLibelle(),
                                        option.getUrl()
                                ))
                                .toList()
                ))
                .toList();

        return new FormulaireCompletResponseDto(
                formulaire.getId(),
                formulaire.getLibelle(),
                formulaire.getDescription(),
            formulaire.getAuteur().getId(),
            formulaire.getEtat() == null ? com.pii.pii_back.entity.enums.FormulaireEtat.PENDING.name() : formulaire.getEtat().name(),
                formulaire.getShareToken(),
            questionDtos
        );
    }

    @Transactional
    public void deleteFormulaire(Long formulaireId) {
        if (!formulaireRepository.existsById(formulaireId)) {
            throw new EntityNotFoundException("Formulaire non trouve: " + formulaireId);
        }
        formulaireRepository.deleteById(formulaireId);
    }

    @Transactional
    public FormulaireCompletResponseDto updateFormulaire(Long formulaireId, FormulaireCompletRequestDto requestDto) {
        Formulaire formulaire = formulaireRepository.findById(formulaireId)
                .orElseThrow(() -> new EntityNotFoundException("Formulaire non trouve: " + formulaireId));

        formulaire.setLibelle(requestDto.libelle());
        formulaire.setDescription(requestDto.description());
        if (requestDto.etat() != null) {
            try {
                formulaire.setEtat(com.pii.pii_back.entity.enums.FormulaireEtat.valueOf(requestDto.etat()));
            } catch (IllegalArgumentException e) {
                // ignore invalid state
            }
        }
        ensureShareToken(formulaire);

        // Créer la nouvelle liste de questions
        List<Question> newQuestions = new ArrayList<>();
        int questionIndex = 0;
        for (FormulaireCompletRequestDto.QuestionCreationDto questionDto : requestDto.questions()) {
            Question question = new Question();
            question.setQuestion(questionDto.question());
            question.setDescription(questionDto.description());
            question.setType(questionDto.type());
            question.setPositionIndex(questionDto.positionIndex() != null ? questionDto.positionIndex() : questionIndex++);
            
            // Set default min/max values for sliders if not provided
            if (questionDto.type() == QuestionType.SPIDER_INT) {
                question.setMinValue(questionDto.minValue() != null ? questionDto.minValue() : 0);
                question.setMaxValue(questionDto.maxValue() != null ? questionDto.maxValue() : 5);
            } else {
                question.setMinValue(questionDto.minValue());
                question.setMaxValue(questionDto.maxValue());
            }
            
            question.setMediaImage(questionDto.mediaImage());
            question.setFormulaire(formulaire);

            List<FormulaireCompletRequestDto.OptionCreationDto> requestOptions = questionDto.options() == null
                    ? List.of()
                    : questionDto.options();

            if (requiresOptions(questionDto.type()) && requestOptions.isEmpty()) {
                throw new IllegalArgumentException("La question de type " + questionDto.type() + " doit contenir des options.");
            }

            List<Options> options = new ArrayList<>();
            for (FormulaireCompletRequestDto.OptionCreationDto optionDto : requestOptions) {
                Options option = new Options();
                option.setLibelle(optionDto.libelle());
                option.setUrl(optionDto.url());
                option.setQuestion(question);
                options.add(option);
            }

            question.setOptions(options);
            newQuestions.add(question);
        }

        // Vider et ajouter les nouvelles questions
        formulaire.getQuestions().clear();
        formulaire.getQuestions().addAll(newQuestions);
        
        Formulaire updated = formulaireRepository.saveAndFlush(formulaire);
        return toResponse(updated);
    }

    @Transactional
    public FormulaireCompletResponseDto duplicateFormulaire(Long formulaireId) {
        Formulaire original = formulaireRepository.findById(formulaireId)
                .orElseThrow(() -> new EntityNotFoundException("Formulaire non trouve: " + formulaireId));

        // Créer une nouvelle copie du formulaire
        Formulaire copy = new Formulaire();
        copy.setLibelle(original.getLibelle() + " (Copie)");
        copy.setDescription(original.getDescription());
        copy.setAuteur(original.getAuteur());

        // Copier les questions
        List<Question> copiedQuestions = new ArrayList<>();
        int questionIndex = 0;
        for (Question originalQuestion : original.getQuestions()) {
            Question questionCopy = new Question();
            questionCopy.setQuestion(originalQuestion.getQuestion());
            questionCopy.setDescription(originalQuestion.getDescription());
            questionCopy.setType(originalQuestion.getType());
            questionCopy.setPositionIndex(originalQuestion.getPositionIndex() != null ? originalQuestion.getPositionIndex() : questionIndex++);
            
            // Set default min/max values for sliders if not present
            if (originalQuestion.getType() == QuestionType.SPIDER_INT) {
                questionCopy.setMinValue(originalQuestion.getMinValue() != null ? originalQuestion.getMinValue() : 0);
                questionCopy.setMaxValue(originalQuestion.getMaxValue() != null ? originalQuestion.getMaxValue() : 5);
            } else {
                questionCopy.setMinValue(originalQuestion.getMinValue());
                questionCopy.setMaxValue(originalQuestion.getMaxValue());
            }
            
            questionCopy.setMediaImage(originalQuestion.getMediaImage());
            questionCopy.setFormulaire(copy);

            // Copier les options
            List<Options> copiedOptions = new ArrayList<>();
            for (Options originalOption : originalQuestion.getOptions()) {
                Options optionCopy = new Options();
                optionCopy.setLibelle(originalOption.getLibelle());
                optionCopy.setUrl(originalOption.getUrl());
                optionCopy.setQuestion(questionCopy);
                copiedOptions.add(optionCopy);
            }

            questionCopy.setOptions(copiedOptions);
            copiedQuestions.add(questionCopy);
        }

        copy.setQuestions(copiedQuestions);
        // set duplicated form to PENDING to avoid accidental public exposure
        copy.setEtat(com.pii.pii_back.entity.enums.FormulaireEtat.PENDING);
        ensureShareToken(copy);
        Formulaire saved = formulaireRepository.save(copy);
        return toResponse(saved);
    }

    @Transactional
    public FormulaireCompletResponseDto updateEtat(Long formulaireId, String etat) {
        Formulaire formulaire = formulaireRepository.findById(formulaireId)
                .orElseThrow(() -> new EntityNotFoundException("Formulaire non trouve: " + formulaireId));

        if (etat != null) {
            try {
                formulaire.setEtat(com.pii.pii_back.entity.enums.FormulaireEtat.valueOf(etat));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Etat invalide: " + etat);
            }
        }

        Formulaire saved = formulaireRepository.saveAndFlush(formulaire);
        return toResponse(saved);
    }

    private void ensureShareToken(Formulaire formulaire) {
        if (formulaire.getShareToken() == null || formulaire.getShareToken().isBlank()) {
            formulaire.setShareToken(UUID.randomUUID().toString());
        }
    }

    @Transactional
    public List<FormulaireListItemResponseDto> getPublicFormulaires() {
        return formulaireRepository.findAll().stream()
                .filter(f -> f.getEtat() == com.pii.pii_back.entity.enums.FormulaireEtat.PUBLISHED)
            .peek(this::ensureShareToken)
                .map(formulaire -> new FormulaireListItemResponseDto(
                        formulaire.getId(),
                        formulaire.getLibelle(),
                        formulaire.getDescription(),
                        formulaire.getAuteur().getId(),
                        formulaire.getAuteur().getNomUtilisateur(),
                        formulaire.getQuestions() == null ? 0 : formulaire.getQuestions().size(),
                        reponseRepository.countByFormulaireId(formulaire.getId()),
                formulaire.getEtat().name(),
                formulaire.getShareToken()
                ))
                .toList();
    }

    @Transactional
    public FormulaireCompletResponseDto getPublicFormulaireById(Long formulaireId, String shareToken) {
        Formulaire formulaire = formulaireRepository.findById(formulaireId)
                .orElseThrow(() -> new EntityNotFoundException("Formulaire non trouve: " + formulaireId));
        ensureShareToken(formulaire);
        boolean sharedPrivateAccess = formulaire.getEtat() == com.pii.pii_back.entity.enums.FormulaireEtat.PRIVATE
                && shareToken != null
                && !shareToken.isBlank()
                && shareToken.equals(formulaire.getShareToken());

        if (formulaire.getEtat() != com.pii.pii_back.entity.enums.FormulaireEtat.PUBLISHED && !sharedPrivateAccess) {
            throw new EntityNotFoundException("Formulaire non trouve: " + formulaireId);
        }
        return toResponse(formulaire);
    }

    @Transactional
    public void submitResponses(Long formulaireId, String responseId, List<SubmitResponsesRequestDto.ResponseItemDto> responses) {
        Formulaire formulaire = formulaireRepository.findById(formulaireId)
                .orElseThrow(() -> new EntityNotFoundException("Formulaire non trouve: " + formulaireId));

        for (SubmitResponsesRequestDto.ResponseItemDto responseDto : responses) {
            Question question = formulaire.getQuestions().stream()
                    .filter(q -> q.getId().equals(responseDto.questionId()))
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Question non trouve: " + responseDto.questionId()));

            Reponse reponse = new Reponse();
            reponse.setQuestion(question);
            reponse.setReponses(responseDto.responseText());
            if (responseId != null && !responseId.isBlank()) {
                reponse.setResponseId(responseId);
            }
            reponseRepository.save(reponse);
        }
    }
}

