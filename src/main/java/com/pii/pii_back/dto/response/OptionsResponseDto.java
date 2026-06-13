package com.pii.pii_back.dto.response;

public record OptionsResponseDto(
        Long id,
        String libelle,
        String url,
        Long questionId
) {
}
