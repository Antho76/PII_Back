package com.pii.pii_back.dto.request;

import com.pii.pii_back.entity.enums.QuestionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record QuestionRequestDto(
        @NotBlank String question,
        @NotNull QuestionType type,
        String mediaImage,
        @NotNull Long formulaireId
) {
}
