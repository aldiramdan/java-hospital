package com.aldiramdan.hospital.service.impl;

import com.aldiramdan.hospital.model.dto.request.MedicineRequest;
import com.aldiramdan.hospital.model.dto.response.ResponseData;
import com.aldiramdan.hospital.model.dto.response.ResponseMedicine;
import com.aldiramdan.hospital.model.entity.Medicine;
import com.aldiramdan.hospital.repository.MedicineRepository;
import com.aldiramdan.hospital.service.MedicineService;
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
public class MedicineServiceImpl implements MedicineService {
    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private MedicineValidator medicineValidator;

    private ResponseData responseData;

    private ResponseMedicine responseMedicine;

    @Override
    public ResponseData getAll() {
        List<Medicine> listMedicine = medicineRepository.findAll();
        List<ResponseMedicine> result = ResponseMedicine.list(listMedicine);
        return responseData = new ResponseData(200, "Success", result);
    }

    @Override
    public ResponseData getById(String id) throws Exception {
        Optional<Medicine> findMedicine = medicineRepository.findById(id);
        medicineValidator.validateMedicineNotFound(findMedicine);
        responseMedicine = new ResponseMedicine(findMedicine.get());
        return responseData = new ResponseData(200, "Success", responseMedicine);
    }

    @Override
    public ResponseData getByName(String name) {
        List<Medicine> listMedicine = new ArrayList<>();

        if (name.isEmpty()) {
            listMedicine = Collections.emptyList();
        } else {
            listMedicine = medicineRepository.findByNameContaining(name);
        }

        List<ResponseMedicine> result = ResponseMedicine.list(listMedicine);
        return responseData = new ResponseData(200, "Success", result);
    }

    @Override
    public ResponseData add(MedicineRequest request) throws Exception {
        Optional<Medicine> findMedicine = medicineRepository.findByName(request.getName());
        medicineValidator.validateMedicineIsExists(findMedicine);

        Medicine medicine = new Medicine();
        medicine.setName(request.getName());
        medicine.setPrice(request.getPrice());

        Medicine result = medicineRepository.save(medicine);

        responseMedicine = new ResponseMedicine(result);
        return responseData = new ResponseData(201, "Success", responseMedicine);
    }

    @Override
    public ResponseData update(String id, MedicineRequest request) throws Exception {
        Optional<Medicine> findMedicine = medicineRepository.findById(id);
        medicineValidator.validateMedicineNotFound(findMedicine);
        medicineValidator.validateMedicineIsAlreadyDeleted(findMedicine.get());

        if (!request.getName().equals(findMedicine.get().getName())) {
            Optional<Medicine> findMedicineByName = medicineRepository.findByName(request.getName());
            medicineValidator.validateMedicineIsExists(findMedicineByName);
        }

        Medicine medicine = findMedicine.get();
        medicine.setName(request.getName());
        medicine.setPrice(request.getPrice());

        Medicine result = medicineRepository.save(medicine);

        responseMedicine = new ResponseMedicine(result);
        return responseData = new ResponseData(200, "Success", responseMedicine);
    }

    @Override
    public ResponseData delete(String id) throws Exception {
        Optional<Medicine> findMedicine = medicineRepository.findById(id);
        medicineValidator.validateMedicineNotFound(findMedicine);

        Medicine medicine = findMedicine.get();
        medicineValidator.validateMedicineIsAlreadyDeleted(medicine);

        medicine.setIsDeleted(true);

        medicineRepository.save(medicine);

        return responseData = new ResponseData(200, "Successfully deleted medicine", null);
    }

    @Override
    public ResponseData recovery(String id) throws Exception {
        Optional<Medicine> findMedicine = medicineRepository.findById(id);
        medicineValidator.validateMedicineNotFound(findMedicine);

        Medicine medicine = findMedicine.get();
        medicineValidator.validateMedicineIsAlreadyRecovered(medicine);

        medicine.setIsDeleted(false);

        medicineRepository.save(medicine);

        return responseData = new ResponseData(200, "Successfully recovered medicine", null);
    }
}
