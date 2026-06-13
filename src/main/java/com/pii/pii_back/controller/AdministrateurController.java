package com.pii.pii_back.controller;

import com.pii.pii_back.dto.request.AdministrateurRequestDto;
import com.pii.pii_back.dto.response.AdministrateurResponseDto;
import com.pii.pii_back.entity.Administrateur;
import com.pii.pii_back.service.AdministrateurService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
public class AdministrateurController {

    private final AdministrateurService administrateurService;

    public AdministrateurController(AdministrateurService administrateurService) {
        this.administrateurService = administrateurService;
    }

    @GetMapping
    public List<AdministrateurResponseDto> getAll() {
        return administrateurService.getAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdministrateurResponseDto create(@Valid @RequestBody AdministrateurRequestDto requestDto) {
        Administrateur administrateur = new Administrateur();
        administrateur.setNomUtilisateur(requestDto.nomUtilisateur());
        administrateur.setMotDePasse(requestDto.motDePasse());
        return toResponse(administrateurService.create(administrateur));
    }

    @PutMapping("/{id}")
    public AdministrateurResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody AdministrateurRequestDto requestDto) {
        Administrateur administrateur = new Administrateur();
        administrateur.setNomUtilisateur(requestDto.nomUtilisateur());
        administrateur.setMotDePasse(requestDto.motDePasse());
        return toResponse(administrateurService.update(id, administrateur));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        administrateurService.delete(id);
    }

    private AdministrateurResponseDto toResponse(Administrateur administrateur) {
        return new AdministrateurResponseDto(administrateur.getId(), administrateur.getNomUtilisateur());
    }
}
