package com.pii.pii_back.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FormulaireRequestDto(
        @NotBlank String libelle,
        @NotNull Long auteurId
) {
}
