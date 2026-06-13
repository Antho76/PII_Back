package com.pii.pii_back.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank String nomUtilisateur,
        @NotBlank String motDePasse
) {
}
