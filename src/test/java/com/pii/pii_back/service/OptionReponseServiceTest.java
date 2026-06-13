package com.pii.pii_back.service;

import com.pii.pii_back.entity.OptionReponse;
import com.pii.pii_back.repository.OptionReponseRepository;
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
class OptionReponseServiceTest {

    @Mock
    private OptionReponseRepository repository;

    @InjectMocks
    private OptionReponseService service;

    @Test
    void createShouldReturnSavedEntity() {
        OptionReponse optionReponse = new OptionReponse();
        optionReponse.setRang(1);

        when(repository.save(optionReponse)).thenReturn(optionReponse);

        OptionReponse result = service.create(optionReponse);

        assertEquals(1, result.getRang());
        verify(repository).save(optionReponse);
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
