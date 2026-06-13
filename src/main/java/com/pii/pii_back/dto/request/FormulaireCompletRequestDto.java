package com.pii.pii_back.dto.request;

import com.pii.pii_back.entity.enums.QuestionType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record FormulaireCompletRequestDto(
        @NotBlank String libelle,
        String description,
        @NotNull Long auteurId,
        // Optional state: PUBLISHED | PENDING | PRIVATE
        String etat,
        @NotEmpty List<@Valid QuestionCreationDto> questions
) {
    public record QuestionCreationDto(
            @NotBlank String question,
            String description,
            @NotNull QuestionType type,
            Integer minValue,
            Integer maxValue,
            String mediaImage,
            Integer positionIndex,
            List<@Valid OptionCreationDto> options
    ) {
    }

    public record OptionCreationDto(
            @NotBlank String libelle,
            String url
    ) {
    }
}
