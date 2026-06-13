package com.pii.pii_back.dto.response;

public record FormulaireResponseDto(
        Long id,
        String libelle,
        String description,
        Long auteurId
) {
}
