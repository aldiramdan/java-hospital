package com.aldiramdan.hospital.validator;

import com.aldiramdan.hospital.exception.custom.NotFoundException;
import com.aldiramdan.hospital.exception.custom.NotProcessException;
import com.aldiramdan.hospital.model.entity.Treatment;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class TreatmentValidator {
    public void validateTreatmentNotFound(Optional<Treatment> findTreatment) throws Exception {
        if (findTreatment.isEmpty()) {
            throw new NotFoundException("Treatment is not found!");
        }
    }

    public void validateTreatmentIsAlreadyDeleted(Treatment treatment) throws Exception {
        if (Objects.nonNull(treatment.getIsDeleted()) && treatment.getIsDeleted()) {
            throw new NotProcessException("Treatment is already deleted!");
        }
    }

    public void validateTreatmentIsAlreadyRecovered(Treatment treatment) throws Exception {
        if (!treatment.getIsDeleted()) {
            throw new NotProcessException("Treatment is already recovered!");
        }
    }
}
