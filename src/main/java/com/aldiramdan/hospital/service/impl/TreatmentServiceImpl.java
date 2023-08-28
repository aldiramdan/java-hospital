package com.aldiramdan.hospital.service.impl;

import com.aldiramdan.hospital.model.dto.request.DiseaseRequest;
import com.aldiramdan.hospital.model.dto.request.TreatmentRequest;
import com.aldiramdan.hospital.model.dto.response.ResponseData;
import com.aldiramdan.hospital.model.dto.response.ResponseTreatment;
import com.aldiramdan.hospital.model.entity.Disease;
import com.aldiramdan.hospital.model.entity.Doctor;
import com.aldiramdan.hospital.model.entity.Patient;
import com.aldiramdan.hospital.model.entity.Treatment;
import com.aldiramdan.hospital.repository.DiseaseRepository;
import com.aldiramdan.hospital.repository.DoctorRepository;
import com.aldiramdan.hospital.repository.PatientRepository;
import com.aldiramdan.hospital.repository.TreatmentRepository;
import com.aldiramdan.hospital.service.TreatmentService;
import com.aldiramdan.hospital.utils.ServiceFee;
import com.aldiramdan.hospital.validator.DiseaseValidator;
import com.aldiramdan.hospital.validator.DoctorValidator;
import com.aldiramdan.hospital.validator.PatientValidator;
import com.aldiramdan.hospital.validator.TreatmentValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TreatmentServiceImpl implements TreatmentService {
    @Autowired
    private TreatmentRepository treatmentRepository;

    @Autowired
    private TreatmentValidator treatmentValidator;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientValidator patientValidator;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorValidator doctorValidator;

    @Autowired
    private DiseaseRepository diseaseRepository;

    @Autowired
    private DiseaseValidator diseaseValidator;

    private ResponseData responseData;

    private ResponseTreatment responseTreatment;

    @Override
    public ResponseData getAll() {
        List<Treatment> listTreatment = treatmentRepository.findAll();
        List<ResponseTreatment> result = ResponseTreatment.list(listTreatment);
        return responseData = new ResponseData(200, "Success", result);
    }

    @Override
    public ResponseData getById(String id) throws Exception {
        Optional<Treatment> findTreatment = treatmentRepository.findById(id);
        treatmentValidator.validateTreatmentNotFound(findTreatment);
        responseTreatment = new ResponseTreatment(findTreatment.get());
        return responseData = new ResponseData(200, "Success", responseTreatment);
    }

    @Override
    public ResponseData getByPatientName(String name) {
        List<Treatment> listTreatment = new ArrayList<>();

        if (name.isEmpty()) {
            listTreatment = Collections.emptyList();
        } else {
            listTreatment = treatmentRepository.findByPatientName(name);
        }

        List<ResponseTreatment> result = ResponseTreatment.list(listTreatment);
        return responseData = new ResponseData(200, "Success", result);
    }

    @Override
    public ResponseData add(TreatmentRequest request) throws Exception {
        Optional<Patient> findPatient = patientRepository.findById(request.getPatientId());
        patientValidator.validatePatientNotFound(findPatient);
        patientValidator.validatePatientIsAlreadyDeleted(findPatient.get());

        Optional<Doctor> findDoctor = doctorRepository.findById(request.getDoctorId());
        doctorValidator.validateDoctorNotFound(findDoctor);
        doctorValidator.validateDoctorIsAlreadyDeleted(findDoctor.get());

        diseaseValidator.validateDiseaseDuplicateName(request.getDisease());

        List<Disease> lisDisease = new ArrayList<>();
        for (DiseaseRequest v : request.getDisease()) {
            Optional<Disease> findDisease = diseaseRepository.findByName(v.getName());
            diseaseValidator.validateDiseaseNotFound(findDisease);
            diseaseValidator.validateDiseaseIsAlreadyDeleted(findDisease.get());

            lisDisease.add(findDisease.get());
        }

        double fee = ServiceFee.sum(lisDisease, findDoctor.get().getConsultationFee());

        Treatment treatment = new Treatment();
        treatment.setPatient(findPatient.get());
        treatment.setDoctor(findDoctor.get());
        treatment.setDisease(lisDisease);
        treatment.setServiceFee(fee);

        Treatment result = treatmentRepository.save(treatment);

        responseTreatment = new ResponseTreatment(result);
        return responseData = new ResponseData(201, "Success", responseTreatment);
    }

    @Override
    public ResponseData update(String id, TreatmentRequest request) throws Exception {
        Optional<Treatment> findTreatment = treatmentRepository.findById(id);
        treatmentValidator.validateTreatmentNotFound(findTreatment);
        treatmentValidator.validateTreatmentIsAlreadyDeleted(findTreatment.get());

        Optional<Patient> findPatient = patientRepository.findById(request.getPatientId());
        patientValidator.validatePatientNotFound(findPatient);
        patientValidator.validatePatientIsAlreadyDeleted(findPatient.get());

        Optional<Doctor> findDoctor = doctorRepository.findById(request.getDoctorId());
        doctorValidator.validateDoctorNotFound(findDoctor);
        doctorValidator.validateDoctorIsAlreadyDeleted(findDoctor.get());

        diseaseValidator.validateDiseaseDuplicateName(request.getDisease());

        List<Disease> lisDisease = new ArrayList<>();
        for (DiseaseRequest v : request.getDisease()) {
            Optional<Disease> findDisease = diseaseRepository.findByName(v.getName());
            diseaseValidator.validateDiseaseNotFound(findDisease);
            diseaseValidator.validateDiseaseIsAlreadyDeleted(findDisease.get());

            lisDisease.add(findDisease.get());
        }

        double fee = ServiceFee.sum(lisDisease, findDoctor.get().getConsultationFee());

        Treatment treatment = findTreatment.get();
        treatment.setPatient(findPatient.get());
        treatment.setDoctor(findDoctor.get());
        treatment.setDisease(lisDisease);
        treatment.setServiceFee(fee);

        Treatment result = treatmentRepository.save(treatment);

        responseTreatment = new ResponseTreatment(result);
        return responseData = new ResponseData(200, "Success", responseTreatment);
    }

    @Override
    public ResponseData delete(String id) throws Exception {
        Optional<Treatment> findTreatment = treatmentRepository.findById(id);
        treatmentValidator.validateTreatmentNotFound(findTreatment);

        Treatment treatment = findTreatment.get();
        treatmentValidator.validateTreatmentIsAlreadyDeleted(treatment);

        treatment.setIsDeleted(true);

        treatmentRepository.save(treatment);

        return responseData = new ResponseData(200, "Successfully deleted treatment", null);
    }

    @Override
    public ResponseData recovery(String id) throws Exception {
        Optional<Treatment> findTreatment = treatmentRepository.findById(id);
        treatmentValidator.validateTreatmentNotFound(findTreatment);

        Treatment treatment = findTreatment.get();
        treatmentValidator.validateTreatmentIsAlreadyRecovered(treatment);

        treatment.setIsDeleted(false);

        treatmentRepository.save(treatment);

        return responseData = new ResponseData(200, "Successfully recovered treatment", null);
    }
}
