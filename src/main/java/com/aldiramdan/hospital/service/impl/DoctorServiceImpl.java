package com.aldiramdan.hospital.service.impl;

import com.aldiramdan.hospital.model.dto.request.DoctorRequest;
import com.aldiramdan.hospital.model.dto.response.ResponseData;
import com.aldiramdan.hospital.model.dto.response.ResponseDoctor;
import com.aldiramdan.hospital.model.entity.Doctor;
import com.aldiramdan.hospital.repository.DoctorRepository;
import com.aldiramdan.hospital.service.DoctorService;
import com.aldiramdan.hospital.validator.DoctorValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DoctorServiceImpl implements DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorValidator doctorValidator;

    private ResponseData responseData;

    private ResponseDoctor responseDoctor;

    @Override
    public ResponseData getAll() {
        List<Doctor> listDoctor = doctorRepository.findAll();
        List<ResponseDoctor> result = ResponseDoctor.list(listDoctor);
        return responseData = new ResponseData(200, "Success", result);
    }

    @Override
    public ResponseData getById(String id) throws Exception {
        Optional<Doctor> findDoctor = doctorRepository.findById(id);
        doctorValidator.validateDoctorNotFound(findDoctor);
        responseDoctor = new ResponseDoctor(findDoctor.get());
        return responseData = new ResponseData(200, "Success", responseDoctor);
    }

    @Override
    public ResponseData getByName(String name) {
        List<Doctor> listDoctor = new ArrayList<>();

        if (name.isEmpty()) {
            listDoctor = Collections.emptyList();
        } else {
            listDoctor = doctorRepository.findByNameContaining(name);
        }

        List<ResponseDoctor> result = ResponseDoctor.list(listDoctor);
        return responseData = new ResponseData(200, "Success", result);
    }

    @Override
    public ResponseData add(DoctorRequest request) throws Exception {
        Doctor doctor = new Doctor();
        doctor.setName(request.getName());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setConsultationFee(request.getConsultationFee());

        Doctor result = doctorRepository.save(doctor);

        responseDoctor = new ResponseDoctor(result);
        return responseData = new ResponseData(201, "Success", responseDoctor);
    }

    @Override
    public ResponseData update(String id, DoctorRequest request) throws Exception {
        Optional<Doctor> findDoctor = doctorRepository.findById(id);
        doctorValidator.validateDoctorNotFound(findDoctor);
        doctorValidator.validateDoctorIsAlreadyDeleted(findDoctor.get());

        Doctor doctor = findDoctor.get();
        doctor.setName(request.getName());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setConsultationFee(request.getConsultationFee());

        Doctor result = doctorRepository.save(doctor);

        responseDoctor = new ResponseDoctor(result);
        return responseData = new ResponseData(200, "Success", responseDoctor);
    }

    @Override
    public ResponseData delete(String id) throws Exception {
        Optional<Doctor> findDoctor = doctorRepository.findById(id);
        doctorValidator.validateDoctorNotFound(findDoctor);

        Doctor doctor = findDoctor.get();
        doctorValidator.validateDoctorIsAlreadyDeleted(doctor);

        doctor.setIsDeleted(true);

        doctorRepository.save(doctor);

        return responseData = new ResponseData(200, "Successfully deleted doctor", null);
    }

    @Override
    public ResponseData recovery(String id) throws Exception {
        Optional<Doctor> findDoctor = doctorRepository.findById(id);
        doctorValidator.validateDoctorNotFound(findDoctor);

        Doctor doctor = findDoctor.get();
        doctorValidator.validateDoctorIsAlreadyRecovered(doctor);

        doctor.setIsDeleted(false);

        doctorRepository.save(doctor);

        return responseData = new ResponseData(200, "Successfully recovered doctor", null);
    }
}
