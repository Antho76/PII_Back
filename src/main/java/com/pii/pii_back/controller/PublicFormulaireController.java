package com.pii.pii_back.controller;

import com.pii.pii_back.dto.request.SubmitResponsesRequestDto;
import com.pii.pii_back.dto.response.FormulaireCompletResponseDto;
import com.pii.pii_back.dto.response.FormulaireListItemResponseDto;
import com.pii.pii_back.service.FormulaireAdminService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/formulaires")
public class PublicFormulaireController {

    private final FormulaireAdminService formulaireAdminService;

    public PublicFormulaireController(FormulaireAdminService formulaireAdminService) {
        this.formulaireAdminService = formulaireAdminService;
    }

    @GetMapping
    public List<FormulaireListItemResponseDto> getAll() {
        return formulaireAdminService.getPublicFormulaires();
    }

    @GetMapping("/{id}")
    public FormulaireCompletResponseDto getById(@PathVariable Long id, @RequestParam(required = false) String shareToken) {
        return formulaireAdminService.getPublicFormulaireById(id, shareToken);
    }

    @PostMapping("/{id}/responses")
    @ResponseStatus(HttpStatus.CREATED)
    public void submitResponses(@PathVariable Long id, @RequestBody SubmitResponsesRequestDto requestDto) {
        formulaireAdminService.submitResponses(id, requestDto.responseId(), requestDto.responses());
    }
}

