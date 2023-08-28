package com.aldiramdan.hospital.service;

import com.aldiramdan.hospital.model.dto.request.DoctorRequest;
import com.aldiramdan.hospital.model.dto.response.ResponseData;

public interface DoctorService {
    ResponseData getAll();

    ResponseData getById(String id) throws Exception;

    ResponseData getByName(String name);

    ResponseData add(DoctorRequest request) throws Exception;

    ResponseData update(String id, DoctorRequest request) throws Exception;

    ResponseData delete(String id) throws Exception;

    ResponseData recovery(String id) throws Exception;
}
