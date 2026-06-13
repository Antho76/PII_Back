package com.pii.pii_back.dto.request;

import jakarta.validation.constraints.NotNull;

public record OptionReponseRequestDto(
        @NotNull Integer rang,
        @NotNull Long optionId,
        @NotNull Long reponseId
) {
}
