package com.aldiramdan.hospital.model.dto.response;

import com.aldiramdan.hospital.model.entity.Doctor;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ResponseDoctor {
    private String id;
    private String name;
    private String specialization;
    private double consultationFee;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date updatedAt;

    public ResponseDoctor(Doctor doctor) {
        this.id = doctor.getId();
        this.name = doctor.getName();
        this.specialization = doctor.getSpecialization();
        this.consultationFee = doctor.getConsultationFee();
        this.createdAt = doctor.getCreatedAt();
        this.updatedAt = doctor.getUpdatedAt();
    }

    public static List<ResponseDoctor> list(List<Doctor> listDoctor) {
        List<ResponseDoctor> list = new ArrayList<>();
        for (Doctor v: listDoctor) {
            list.add(new ResponseDoctor(v));
        }
        return list;
    }
}
