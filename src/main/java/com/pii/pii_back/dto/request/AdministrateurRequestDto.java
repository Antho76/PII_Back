package com.pii.pii_back.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AdministrateurRequestDto(
        @NotBlank String nomUtilisateur,
        @NotBlank String motDePasse
) {
}
