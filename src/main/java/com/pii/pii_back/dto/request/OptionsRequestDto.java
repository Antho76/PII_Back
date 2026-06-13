package com.pii.pii_back.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OptionsRequestDto(
        @NotBlank String libelle,
        String url,
        @NotNull Long questionId
) {
}
