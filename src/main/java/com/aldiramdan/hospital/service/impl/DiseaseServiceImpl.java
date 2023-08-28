package com.aldiramdan.hospital.service.impl;

import com.aldiramdan.hospital.model.dto.request.DiseaseRequest;
import com.aldiramdan.hospital.model.dto.response.ResponseData;
import com.aldiramdan.hospital.model.dto.response.ResponseDisease;
import com.aldiramdan.hospital.model.entity.Disease;
import com.aldiramdan.hospital.model.entity.Medicine;
import com.aldiramdan.hospital.repository.DiseaseRepository;
import com.aldiramdan.hospital.repository.MedicineRepository;
import com.aldiramdan.hospital.service.DiseaseService;
import com.aldiramdan.hospital.validator.DiseaseValidator;
import com.aldiramdan.hospital.validator.MedicineValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DiseaseServiceImpl implements DiseaseService {
    @Autowired
    private DiseaseRepository diseaseRepository;

    @Autowired
    private DiseaseValidator diseaseValidator;

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private MedicineValidator medicineValidator;

    private ResponseData responseData;

    private ResponseDisease responseDisease;

    @Override
    public ResponseData getAll() {
        List<Disease> listDisease = diseaseRepository.findAll();
        List<ResponseDisease> result = ResponseDisease.list(listDisease);
        return responseData = new ResponseData(200, "Success", result);
    }

    @Override
    public ResponseData getById(String id) throws Exception {
        Optional<Disease> findDisease = diseaseRepository.findById(id);
        diseaseValidator.validateDiseaseNotFound(findDisease);
        responseDisease = new ResponseDisease(findDisease.get());
        return responseData = new ResponseData(200, "Success", responseDisease);
    }

    @Override
    public ResponseData getByName(String name) {
        List<Disease> listDisease = new ArrayList<>();

         if (name.isEmpty()) {
             listDisease = Collections.emptyList();
         } else {
             listDisease = diseaseRepository.findByNameContaining(name);
         }

        List<ResponseDisease> result = ResponseDisease.list(listDisease);
        return responseData = new ResponseData(200, "Success", result);
    }

    @Override
    public ResponseData add(DiseaseRequest request) throws Exception {
        Optional<Disease> findDisease = diseaseRepository.findByName(request.getName());
        diseaseValidator.validateDiseaseIsExists(findDisease);

        Optional<Medicine> findMedicine = medicineRepository.findByName(request.getMedicine());
        medicineValidator.validateMedicineNotFound(findMedicine);
        medicineValidator.validateMedicineIsAlreadyDeleted(findMedicine.get());

        Disease disease = new Disease();
        disease.setName(request.getName());
        disease.setMedicine(findMedicine.get());

        Disease result = diseaseRepository.save(disease);

        responseDisease = new ResponseDisease(result);
        return responseData = new ResponseData(201, "Success", responseDisease);
    }

    @Override
    public ResponseData update(String id, DiseaseRequest request) throws Exception {
        Optional<Disease> findDisease = diseaseRepository.findById(id);
        diseaseValidator.validateDiseaseNotFound(findDisease);
        diseaseValidator.validateDiseaseIsAlreadyDeleted(findDisease.get());

        if (!request.getName().equals(findDisease.get().getName())) {
            Optional<Disease> findDiseaseByName = diseaseRepository.findByName(request.getName());
            diseaseValidator.validateDiseaseIsExists(findDiseaseByName);
        }

        Optional<Medicine> findMedicine = medicineRepository.findByName(request.getMedicine());
        medicineValidator.validateMedicineNotFound(findMedicine);
        medicineValidator.validateMedicineIsAlreadyDeleted(findMedicine.get());

        Disease disease = findDisease.get();
        disease.setName(request.getName());
        disease.setMedicine(findMedicine.get());

        Disease result = diseaseRepository.save(disease);

        responseDisease = new ResponseDisease(result);
        return responseData = new ResponseData(200, "Success", responseDisease);
    }

    @Override
    public ResponseData delete(String id) throws Exception {
        Optional<Disease> findDisease = diseaseRepository.findById(id);
        diseaseValidator.validateDiseaseNotFound(findDisease);

        Disease disease = findDisease.get();
        diseaseValidator.validateDiseaseIsAlreadyDeleted(disease);

        disease.setIsDeleted(true);

        diseaseRepository.save(disease);

        return responseData = new ResponseData(200, "Successfully deleted disease", null);
    }

    @Override
    public ResponseData recovery(String id) throws Exception {
        Optional<Disease> findDisease = diseaseRepository.findById(id);
        diseaseValidator.validateDiseaseNotFound(findDisease);

        Disease disease = findDisease.get();
        diseaseValidator.validateDiseaseIsAlreadyRecovered(disease);

        disease.setIsDeleted(false);

        diseaseRepository.save(disease);

        return responseData = new ResponseData(200, "Successfully recovered disease", null);
    }
}
