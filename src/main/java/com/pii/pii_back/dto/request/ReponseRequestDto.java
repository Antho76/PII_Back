package com.pii.pii_back.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReponseRequestDto(
        @NotBlank String reponses,
        @NotNull Long questionId
) {
}
