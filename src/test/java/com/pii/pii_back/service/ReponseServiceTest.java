package com.pii.pii_back.service;

import com.pii.pii_back.entity.Reponse;
import com.pii.pii_back.repository.ReponseRepository;
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
class ReponseServiceTest {

    @Mock
    private ReponseRepository repository;

    @InjectMocks
    private ReponseService service;

    @Test
    void createShouldReturnSavedEntity() {
        Reponse reponse = new Reponse();
        reponse.setReponses("OK");

        when(repository.save(reponse)).thenReturn(reponse);

        Reponse result = service.create(reponse);

        assertEquals("OK", result.getReponses());
        verify(repository).save(reponse);
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
