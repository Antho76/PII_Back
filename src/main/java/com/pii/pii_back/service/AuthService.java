package com.pii.pii_back.service;

import com.pii.pii_back.dto.request.LoginRequestDto;
import com.pii.pii_back.dto.response.LoginResponseDto;
import com.pii.pii_back.entity.Administrateur;
import com.pii.pii_back.repository.AdministrateurRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final AdministrateurRepository administrateurRepository;
    private final AuthSessionService authSessionService;

    public AuthService(
            AdministrateurRepository administrateurRepository,
            AuthSessionService authSessionService
    ) {
        this.administrateurRepository = administrateurRepository;
        this.authSessionService = authSessionService;
    }

    public LoginResponseDto login(LoginRequestDto requestDto) {
        Administrateur administrateur = administrateurRepository
                .findByNomUtilisateurAndMotDePasse(requestDto.nomUtilisateur(), requestDto.motDePasse())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Identifiants invalides."));

        String token = authSessionService.createSession(administrateur.getId());
        return new LoginResponseDto(token, administrateur.getId(), administrateur.getNomUtilisateur());
    }

    public boolean isTokenValid(String token) {
        return authSessionService.isValidToken(token);
    }

    public void logout(String token) {
        authSessionService.invalidateSession(token);
    }
}
