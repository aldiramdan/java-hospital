package com.aldiramdan.hospital.validator;

import com.aldiramdan.hospital.exception.custom.ConflictException;
import com.aldiramdan.hospital.exception.custom.NotFoundException;
import com.aldiramdan.hospital.exception.custom.NotProcessException;
import com.aldiramdan.hospital.model.entity.Medicine;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class MedicineValidator {
    public void validateMedicineNotFound(Optional<Medicine> findMedicine) throws Exception {
        if (findMedicine.isEmpty()) {
            throw new NotFoundException("Medicine is not found!");
        }
    }

    public void validateMedicineIsExists(Optional<Medicine> findMedicine) throws Exception {
        if (findMedicine.isPresent()) {
            throw new ConflictException("Medicine name has been exists");
        }
    }

    public void validateMedicineIsAlreadyDeleted(Medicine medicine) throws Exception {
        if (Objects.nonNull(medicine.getIsDeleted()) && medicine.getIsDeleted()) {
            throw new NotProcessException("Medicine is already deleted!");
        }
    }

    public void validateMedicineIsAlreadyRecovered(Medicine medicine) throws Exception {
        if (!medicine.getIsDeleted()) {
            throw new NotProcessException("Medicine is already recovered!");
        }
    }
}
