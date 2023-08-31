package com.aldiramdan.hospital.validator;

import com.aldiramdan.hospital.exception.custom.NotFoundException;
import com.aldiramdan.hospital.exception.custom.NotProcessException;
import com.aldiramdan.hospital.model.entity.Doctor;
import com.aldiramdan.hospital.repository.DoctorRepository;
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
public class DoctorValidatorTest {
    @InjectMocks
    private DoctorValidator doctorValidator;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testValidateDoctor_thenNotFound() {
        DoctorRepository doctorRepository = mock(DoctorRepository.class);
        Optional<Doctor> findDoctor = Optional.empty();
        lenient().when(doctorRepository.findById(UUID.randomUUID().toString())).thenReturn(findDoctor);

        assertThrows(NotFoundException.class, () -> {
            doctorValidator.validateDoctorNotFound(findDoctor);
        });
    }

    @Test
    public void testValidateDoctor_thenIsAlreadyDeleted() {
        Doctor doctor = new Doctor();
        doctor.setIsDeleted(true);

        assertThrows(NotProcessException.class, () -> {
            doctorValidator.validateDoctorIsAlreadyDeleted(doctor);
        });
    }

    @Test
    public void testValidateDoctor_thenIsAlreadyRecovered() {
        Doctor doctor = new Doctor();
        doctor.setIsDeleted(false);

        assertThrows(NotProcessException.class, () -> {
            doctorValidator.validateDoctorIsAlreadyRecovered(doctor);
        });
    }
}
