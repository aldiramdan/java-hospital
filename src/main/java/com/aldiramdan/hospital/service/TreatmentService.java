package com.aldiramdan.hospital.service;

import com.aldiramdan.hospital.model.dto.request.TreatmentRequest;
import com.aldiramdan.hospital.model.dto.response.ResponseData;

public interface TreatmentService {
    ResponseData getAll();

    ResponseData getById(String id) throws Exception;

    ResponseData getByPatientName(String name);

    ResponseData add(TreatmentRequest request) throws Exception;

    ResponseData update(String id, TreatmentRequest request) throws Exception;

    ResponseData delete(String id) throws Exception;

    ResponseData recovery(String id) throws Exception;
}
