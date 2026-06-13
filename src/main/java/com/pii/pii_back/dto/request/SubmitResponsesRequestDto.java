package com.pii.pii_back.dto.request;

import java.util.List;

public record SubmitResponsesRequestDto(
        Long formulaireId,
        String responseId,
        List<ResponseItemDto> responses
) {
    public record ResponseItemDto(
            Long questionId,
            String responseText
    ) {
    }
}
