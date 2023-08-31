package com.aldiramdan.hospital.validator;

import com.aldiramdan.hospital.exception.custom.NotFoundException;
import com.aldiramdan.hospital.exception.custom.NotProcessException;
import com.aldiramdan.hospital.model.entity.Treatment;
import com.aldiramdan.hospital.repository.TreatmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class TreatmentValidatorTest {
    @InjectMocks
    private TreatmentValidator treatmentValidator;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testValidateTreatment_thenNotFound() {
        TreatmentRepository treatmentRepository = mock(TreatmentRepository.class);
        Optional<Treatment> findTreatment = Optional.empty();
        lenient().when(treatmentRepository.findById(UUID.randomUUID().toString())).thenReturn(findTreatment);

        assertThrows(NotFoundException.class, () -> {
            treatmentValidator.validateTreatmentNotFound(findTreatment);
        });
    }

    @Test
    public void testValidateTreatment_thenIsAlreadyDeleted() {
        Treatment treatment = new Treatment();
        treatment.setIsDeleted(true);

        assertThrows(NotProcessException.class, () -> {
            treatmentValidator.validateTreatmentIsAlreadyDeleted(treatment);
        });
    }

    @Test
    public void testValidateTreatment_thenIsAlreadyRecovered() {
        Treatment treatment = new Treatment();
        treatment.setIsDeleted(false);

        assertThrows(NotProcessException.class, () -> {
            treatmentValidator.validateTreatmentIsAlreadyRecovered(treatment);
        });
    }
}
