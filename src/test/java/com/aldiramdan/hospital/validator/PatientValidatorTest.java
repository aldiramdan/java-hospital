package com.aldiramdan.hospital.validator;

import com.aldiramdan.hospital.exception.custom.NotFoundException;
import com.aldiramdan.hospital.exception.custom.NotProcessException;
import com.aldiramdan.hospital.model.entity.Patient;
import com.aldiramdan.hospital.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientValidatorTest {
    @InjectMocks
    private PatientValidator patientValidator;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testValidatePatient_thenNotFound() {
        PatientRepository patientRepository = mock(PatientRepository.class);
        Optional<Patient> findPatient = Optional.empty();
        lenient().when(patientRepository.findById(UUID.randomUUID().toString())).thenReturn(findPatient);

        assertThrows(NotFoundException.class, () -> {
            patientValidator.validatePatientNotFound(findPatient);
        });
    }

    @Test
    public void testValidatePatient_thenIsAlreadyDeleted() {
        Patient patient = new Patient();
        patient.setIsDeleted(true);

        assertThrows(NotProcessException.class, () -> {
            patientValidator.validatePatientIsAlreadyDeleted(patient);
        });
    }

    @Test
    public void testValidatePatient_thenIsAlreadyRecovered() {
        Patient patient = new Patient();
        patient.setIsDeleted(false);

        assertThrows(NotProcessException.class, () -> {
            patientValidator.validatePatientIsAlreadyRecovered(patient);
        });
    }
}

