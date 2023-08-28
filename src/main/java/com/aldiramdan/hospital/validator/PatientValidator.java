package com.aldiramdan.hospital.validator;

import com.aldiramdan.hospital.exception.custom.NotFoundException;
import com.aldiramdan.hospital.exception.custom.NotProcessException;
import com.aldiramdan.hospital.model.entity.Patient;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class PatientValidator {
    public void validatePatientNotFound(Optional<Patient> findPatient) throws Exception {
        if (findPatient.isEmpty()) {
            throw new NotFoundException("Patient is not found!");
        }
    }

    public void validatePatientIsAlreadyDeleted(Patient patient) throws Exception {
        if (Objects.nonNull(patient.getIsDeleted()) && patient.getIsDeleted()) {
            throw new NotProcessException("Patient is already deleted!");
        }
    }

    public void validatePatientIsAlreadyRecovered(Patient patient) throws Exception {
        if (!patient.getIsDeleted()) {
            throw new NotProcessException("Patient is already recovered!");
        }
    }
}
