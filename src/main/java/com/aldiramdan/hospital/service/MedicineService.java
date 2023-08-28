package com.aldiramdan.hospital.service;

import com.aldiramdan.hospital.model.dto.request.MedicineRequest;
import com.aldiramdan.hospital.model.dto.response.ResponseData;

public interface MedicineService {
    ResponseData getAll();

    ResponseData getById(String id) throws Exception;

    ResponseData getByName(String name);

    ResponseData add(MedicineRequest request) throws Exception;

    ResponseData update(String id, MedicineRequest request) throws Exception;

    ResponseData delete(String id) throws Exception;

    ResponseData recovery(String id) throws Exception;
}
