package com.pii.pii_back.service;

import com.pii.pii_back.entity.Administrateur;
import com.pii.pii_back.repository.AdministrateurRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdministrateurServiceTest {

    @Mock
    private AdministrateurRepository repository;

    @InjectMocks
    private AdministrateurService service;

    @Test
    void createShouldReturnSavedEntity() {
        Administrateur admin = new Administrateur();
        admin.setNomUtilisateur("admin");
        admin.setMotDePasse("pwd");

        when(repository.save(admin)).thenReturn(admin);

        Administrateur result = service.create(admin);

        assertEquals("admin", result.getNomUtilisateur());
        verify(repository).save(admin);
    }

    @Test
    void getByIdShouldThrowWhenMissing() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.getById(99L));
    }

    @Test
    void deleteShouldCallRepositoryWhenEntityExists() {
        when(repository.existsById(1L)).thenReturn(true);

        service.delete(1L);

        verify(repository).deleteById(1L);
    }
}
