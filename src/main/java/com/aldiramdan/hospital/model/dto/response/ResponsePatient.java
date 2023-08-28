package com.aldiramdan.hospital.model.dto.response;

import com.aldiramdan.hospital.model.entity.Patient;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ResponsePatient {
    private String id;
    private String name;
    private int age;
    private String address;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date updatedAt;

    public ResponsePatient(Patient patient) {
        this.id = patient.getId();
        this.name = patient.getName();
        this.age = patient.getAge();
        this.address = patient.getAddress();
        this.createdAt = patient.getCreatedAt();
        this.updatedAt = patient.getUpdatedAt();
    }

    public static List<ResponsePatient> list(List<Patient> listPatient) {
        List<ResponsePatient> list = new ArrayList<>();
        for (Patient v: listPatient) {
            list.add(new ResponsePatient(v));
        }
        return list;
    }
}
