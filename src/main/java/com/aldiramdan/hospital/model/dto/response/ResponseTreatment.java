package com.aldiramdan.hospital.model.dto.response;

import com.aldiramdan.hospital.model.entity.Disease;
import com.aldiramdan.hospital.model.entity.Treatment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ResponseTreatment {
    private String id;
    private ResponsePatient patient;
    private ResponseDoctor doctor;
    private List<ResponseDisease> disease;
    private double serviceFee;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date updatedAt;

    public ResponseTreatment(Treatment treatment) {
        this.id = treatment.getId();
        this.patient = new ResponsePatient(treatment.getPatient());
        this.doctor = new ResponseDoctor(treatment.getDoctor());
        
        List<ResponseDisease> listDisease = new ArrayList<>();
        for (Disease v : treatment.getDisease()) {
            listDisease.add(new ResponseDisease(v));
        }
        this.disease = listDisease;
        this.serviceFee = treatment.getServiceFee();
        this.createdAt = treatment.getCreatedAt();
        this.updatedAt = treatment.getUpdatedAt();
    }

    public static List<ResponseTreatment> list(List<Treatment> listTreatment) {
        List<ResponseTreatment> list = new ArrayList<>();
        for (Treatment v: listTreatment) {
            list.add(new ResponseTreatment(v));
        }
        return list;
    }
}
