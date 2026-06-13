package com.pii.pii_back.dto.response;

import com.pii.pii_back.entity.enums.QuestionType;

import java.util.List;

public record FormulaireCompletResponseDto(
        Long id,
        String libelle,
        String description,
        Long auteurId,
        String etat,
        String shareToken,
        List<QuestionDetailDto> questions
) {
    public record QuestionDetailDto(
            Long id,
            String question,
            String description,
            QuestionType type,
            Integer minValue,
            Integer maxValue,
            String mediaImage,
            Integer positionIndex,
            List<OptionDetailDto> options
    ) {
    }

    public record OptionDetailDto(
            Long id,
            String libelle,
            String url
    ) {
    }
}
