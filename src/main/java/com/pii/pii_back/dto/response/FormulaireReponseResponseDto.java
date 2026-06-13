package com.pii.pii_back.dto.response;

import com.pii.pii_back.entity.enums.QuestionType;
import java.util.List;

public record FormulaireReponseResponseDto(
        Long formulaireId,
        String formulaireLibelle,
        long totalResponses,
        List<ResponseItemDto> responses
) {
    public record ResponseItemDto(
            Long responseId,
            String respondentId,
            Long questionId,
            String question,
            QuestionType questionType,
            String mediaImage,
            String responseText
    ) {
    }
}
