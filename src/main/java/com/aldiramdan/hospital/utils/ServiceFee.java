package com.aldiramdan.hospital.utils;

import com.aldiramdan.hospital.model.entity.Disease;

import java.util.List;

public class ServiceFee {
    public static double sum(List<Disease> diseases, double fee) {
        double sum = 0.0;
        for (Disease v : diseases) {
            sum += v.getMedicine().getPrice();
        }
        return sum + fee;
    }
}
