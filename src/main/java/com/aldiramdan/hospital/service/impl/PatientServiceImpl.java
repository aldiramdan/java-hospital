package com.aldiramdan.hospital.service.impl;

import com.aldiramdan.hospital.model.dto.request.PatientRequest;
import com.aldiramdan.hospital.model.dto.response.ResponseData;
import com.aldiramdan.hospital.model.dto.response.ResponsePatient;
import com.aldiramdan.hospital.model.entity.Patient;
import com.aldiramdan.hospital.repository.PatientRepository;
import com.aldiramdan.hospital.service.PatientService;
import com.aldiramdan.hospital.validator.PatientValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PatientServiceImpl implements PatientService {
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientValidator patientValidator;

    private ResponseData responseData;

    private ResponsePatient responsePatient;

    @Override
    public ResponseData getAll() {
        List<Patient> listPatient = patientRepository.findAll();
        List<ResponsePatient> result = ResponsePatient.list(listPatient);
        return responseData = new ResponseData(200, "Success", result);
    }

    @Override
    public ResponseData getById(String id) throws Exception {
        Optional<Patient> findPatient = patientRepository.findById(id);
        patientValidator.validatePatientNotFound(findPatient);
        responsePatient = new ResponsePatient(findPatient.get());
        return responseData = new ResponseData(200, "Success", responsePatient);
    }

    @Override
    public ResponseData getByName(String name) {
        List<Patient> listPatient = new ArrayList<>();

        if (name.isEmpty()) {
            listPatient = Collections.emptyList();
        } else {
            listPatient = patientRepository.findByNameContaining(name);
        }

        List<ResponsePatient> result = ResponsePatient.list(listPatient);
        return responseData = new ResponseData(200, "Success", result);
    }

    @Override
    public ResponseData add(PatientRequest request) throws Exception {
        Patient patient = new Patient();
        patient.setName(request.getName());
        patient.setAge(request.getAge());
        patient.setAddress(request.getAddress());

        Patient result = patientRepository.save(patient);

        responsePatient = new ResponsePatient(result);
        return responseData = new ResponseData(201, "Success", responsePatient);
    }

    @Override
    public ResponseData update(String id, PatientRequest request) throws Exception {
        Optional<Patient> findPatient = patientRepository.findById(id);
        patientValidator.validatePatientNotFound(findPatient);
        patientValidator.validatePatientIsAlreadyDeleted(findPatient.get());

        Patient patient = findPatient.get();
        patient.setName(request.getName());
        patient.setAge(request.getAge());
        patient.setAddress(request.getAddress());

        Patient result = patientRepository.save(patient);

        responsePatient = new ResponsePatient(result);
        return responseData = new ResponseData(200, "Success", responsePatient);
    }

    @Override
    public ResponseData delete(String id) throws Exception {
        Optional<Patient> findPatient = patientRepository.findById(id);
        patientValidator.validatePatientNotFound(findPatient);

        Patient patient = findPatient.get();
        patientValidator.validatePatientIsAlreadyDeleted(patient);

        patient.setIsDeleted(true);

        patientRepository.save(patient);

        return responseData = new ResponseData(200, "Successfully deleted patient", null);
    }

    @Override
    public ResponseData recovery(String id) throws Exception {
        Optional<Patient> findPatient = patientRepository.findById(id);
        patientValidator.validatePatientNotFound(findPatient);

        Patient patient = findPatient.get();
        patientValidator.validatePatientIsAlreadyRecovered(patient);

        patient.setIsDeleted(false);

        patientRepository.save(patient);

        return responseData = new ResponseData(200, "Successfully recovered patient", null);
    }
}
