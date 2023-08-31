package com.aldiramdan.hospital.validator;

import com.aldiramdan.hospital.exception.custom.ConflictException;
import com.aldiramdan.hospital.exception.custom.NotFoundException;
import com.aldiramdan.hospital.exception.custom.NotProcessException;
import com.aldiramdan.hospital.model.entity.Medicine;
import com.aldiramdan.hospital.repository.MedicineRepository;
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
public class MedicineValidatorTest {
    @InjectMocks
    private MedicineValidator medicineValidator;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testValidateMedicine_thenNotFound() {
        MedicineRepository medicineRepository = mock(MedicineRepository.class);
        Optional<Medicine> findMedicine = Optional.empty();
        lenient().when(medicineRepository.findById(UUID.randomUUID().toString())).thenReturn(findMedicine);

        assertThrows(NotFoundException.class, () -> {
            medicineValidator.validateMedicineNotFound(findMedicine);
        });
    }

    @Test
    public void testValidateMedicine_thenIsExists() {
        MedicineRepository medicineRepository = mock(MedicineRepository.class);
        Optional<Medicine> findMedicine = Optional.of(new Medicine());
        lenient().when(medicineRepository.findById(UUID.randomUUID().toString())).thenReturn(findMedicine);

        assertThrows(ConflictException.class, () -> {
            medicineValidator.validateMedicineIsExists(findMedicine);
        });
    }

    @Test
    public void testValidateMedicine_thenIsAlreadyDeleted() {
        Medicine medicine = new Medicine();
        medicine.setIsDeleted(true);

        assertThrows(NotProcessException.class, () -> {
            medicineValidator.validateMedicineIsAlreadyDeleted(medicine);
        });
    }

    @Test
    public void testValidateMedicine_thenIsAlreadyRecovered() {
        Medicine medicine = new Medicine();
        medicine.setIsDeleted(false);

        assertThrows(NotProcessException.class, () -> {
            medicineValidator.validateMedicineIsAlreadyRecovered(medicine);
        });
    }
}
