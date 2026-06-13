package com.pii.pii_back.controller;

import com.pii.pii_back.dto.request.FormulaireCompletRequestDto;
import com.pii.pii_back.dto.response.FormulaireCompletResponseDto;
import com.pii.pii_back.dto.response.FormulaireListItemResponseDto;
import com.pii.pii_back.dto.response.FormulaireReponseResponseDto;
import com.pii.pii_back.service.FormulaireAdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/formulaires")
public class FormulaireAdminController {

    private final FormulaireAdminService formulaireAdminService;

    public FormulaireAdminController(FormulaireAdminService formulaireAdminService) {
        this.formulaireAdminService = formulaireAdminService;
    }

    @GetMapping
    public List<FormulaireListItemResponseDto> getAll() {
        return formulaireAdminService.getFormulaires();
    }

    @GetMapping("/{id}")
    public FormulaireCompletResponseDto getById(@PathVariable Long id) {
        return formulaireAdminService.getFormulaireById(id);
    }

    @GetMapping("/{id}/responses")
    public FormulaireReponseResponseDto getResponses(@PathVariable Long id) {
        return formulaireAdminService.getFormulaireResponses(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FormulaireCompletResponseDto create(@Valid @RequestBody FormulaireCompletRequestDto requestDto) {
        return formulaireAdminService.createFormulaireComplet(requestDto);
    }

    @PutMapping("/{id}")
    public FormulaireCompletResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody FormulaireCompletRequestDto requestDto) {
        return formulaireAdminService.updateFormulaire(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        formulaireAdminService.deleteFormulaire(id);
    }

    @PostMapping("/{id}/duplicate")
    @ResponseStatus(HttpStatus.CREATED)
    public FormulaireCompletResponseDto duplicate(@PathVariable Long id) {
        return formulaireAdminService.duplicateFormulaire(id);
    }

    @PutMapping("/{id}/etat")
    public FormulaireCompletResponseDto updateEtat(@PathVariable Long id, @RequestBody com.pii.pii_back.dto.request.FormulaireEtatRequestDto dto) {
        return formulaireAdminService.updateEtat(id, dto.etat());
    }
}
