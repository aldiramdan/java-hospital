package com.aldiramdan.hospital.model.dto.response;

import com.aldiramdan.hospital.model.entity.Disease;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ResponseDisease {
    private String id;
    private String name;
    private ResponseMedicine medicine;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date updatedAt;

    public ResponseDisease (Disease disease) {
        this.id = disease.getId();
        this.name = disease.getName();
        this.medicine = new ResponseMedicine(disease.getMedicine());
        this.createdAt = disease.getCreatedAt();
        this.updatedAt = disease.getUpdatedAt();
    }

    public static List<ResponseDisease> list(List<Disease> listDisease) {
        List<ResponseDisease> list = new ArrayList<>();
        for (Disease v: listDisease) {
            list.add(new ResponseDisease(v));
        }
        return list;
    }
}
