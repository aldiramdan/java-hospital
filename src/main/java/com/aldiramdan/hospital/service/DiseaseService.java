package com.aldiramdan.hospital.service;

import com.aldiramdan.hospital.model.dto.request.DiseaseRequest;
import com.aldiramdan.hospital.model.dto.response.ResponseData;

public interface DiseaseService {
    ResponseData getAll();

    ResponseData getById(String id) throws Exception;

    ResponseData getByName(String name);

    ResponseData add(DiseaseRequest request) throws Exception;

    ResponseData update(String id, DiseaseRequest request) throws Exception;

    ResponseData delete(String id) throws Exception;

    ResponseData recovery(String id) throws Exception;
}
