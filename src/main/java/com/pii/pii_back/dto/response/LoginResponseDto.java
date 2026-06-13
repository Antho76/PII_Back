package com.pii.pii_back.dto.response;

public record LoginResponseDto(
        String token,
        Long administrateurId,
        String nomUtilisateur
) {
}
