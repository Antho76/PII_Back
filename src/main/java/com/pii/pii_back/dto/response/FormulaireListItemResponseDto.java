package com.pii.pii_back.dto.response;

public record FormulaireListItemResponseDto(
        Long id,
        String libelle,
        String description,
        Long auteurId,
        String auteurNom,
        int questionCount,
        long responseCount,
        String etat,
        String shareToken
) {
}
