package com.aldiramdan.hospital.validator;

import com.aldiramdan.hospital.exception.custom.NotFoundException;
import com.aldiramdan.hospital.exception.custom.NotProcessException;
import com.aldiramdan.hospital.model.entity.Doctor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class DoctorValidator {
    public void validateDoctorNotFound(Optional<Doctor> findDoctor) throws Exception {
        if (findDoctor.isEmpty()) {
            throw new NotFoundException("Doctor is not found!");
        }
    }

    public void validateDoctorIsAlreadyDeleted(Doctor doctor) throws Exception {
        if (Objects.nonNull(doctor.getIsDeleted()) && doctor.getIsDeleted()) {
            throw new NotProcessException("Doctor is already deleted!");
        }
    }

    public void validateDoctorIsAlreadyRecovered(Doctor doctor) throws Exception {
        if (!doctor.getIsDeleted()) {
            throw new NotProcessException("Doctor is already recovered!");
        }
    }
}
