package com.aldiramdan.hospital.service;

import com.aldiramdan.hospital.model.dto.request.PatientRequest;
import com.aldiramdan.hospital.model.dto.response.ResponseData;

public interface PatientService {
    ResponseData getAll();

    ResponseData getById(String id) throws Exception;

    ResponseData getByName(String name);

    ResponseData add(PatientRequest request) throws Exception;

    ResponseData update(String id, PatientRequest request) throws Exception;

    ResponseData delete(String id) throws Exception;

    ResponseData recovery(String id) throws Exception;
}
