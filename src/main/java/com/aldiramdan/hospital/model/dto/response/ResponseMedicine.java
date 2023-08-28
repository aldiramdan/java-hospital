package com.aldiramdan.hospital.model.dto.response;

import com.aldiramdan.hospital.model.entity.Medicine;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ResponseMedicine {
    private String id;
    private String name;
    private double price;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date updatedAt;

    public ResponseMedicine (Medicine medicine) {
        this.id = medicine.getId();
        this.name = medicine.getName();
        this.price = medicine.getPrice();
        this.createdAt = medicine.getCreatedAt();
        this.updatedAt = medicine.getUpdatedAt();
    }

    public static List<ResponseMedicine> list(List<Medicine> listMedicine) {
        List<ResponseMedicine> list = new ArrayList<>();
        for (Medicine v: listMedicine) {
            list.add(new ResponseMedicine(v));
        }
        return list;
    }
}
