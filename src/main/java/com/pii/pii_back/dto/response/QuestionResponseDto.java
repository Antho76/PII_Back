package com.pii.pii_back.dto.response;

import com.pii.pii_back.entity.enums.QuestionType;

public record QuestionResponseDto(
        Long id,
        String question,
        String description,
        QuestionType type,
        Integer minValue,
        Integer maxValue,
        String mediaImage,
        Long formulaireId
) {
}
