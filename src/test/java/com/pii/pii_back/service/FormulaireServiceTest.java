package com.pii.pii_back.service;

import com.pii.pii_back.entity.Formulaire;
import com.pii.pii_back.repository.FormulaireRepository;
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
class FormulaireServiceTest {

    @Mock
    private FormulaireRepository repository;

    @InjectMocks
    private FormulaireService service;

    @Test
    void createShouldReturnSavedEntity() {
        Formulaire formulaire = new Formulaire();
        formulaire.setLibelle("Survey");

        when(repository.save(formulaire)).thenReturn(formulaire);

        Formulaire result = service.create(formulaire);

        assertEquals("Survey", result.getLibelle());
        verify(repository).save(formulaire);
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
