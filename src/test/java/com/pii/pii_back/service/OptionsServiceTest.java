package com.pii.pii_back.service;

import com.pii.pii_back.entity.Options;
import com.pii.pii_back.repository.OptionsRepository;
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
class OptionsServiceTest {

    @Mock
    private OptionsRepository repository;

    @InjectMocks
    private OptionsService service;

    @Test
    void createShouldReturnSavedEntity() {
        Options option = new Options();
        option.setLibelle("Option A");

        when(repository.save(option)).thenReturn(option);

        Options result = service.create(option);

        assertEquals("Option A", result.getLibelle());
        verify(repository).save(option);
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
