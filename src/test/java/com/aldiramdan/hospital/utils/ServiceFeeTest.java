package com.aldiramdan.hospital.utils;

import com.aldiramdan.hospital.model.entity.Disease;
import com.aldiramdan.hospital.model.entity.Medicine;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServiceFeeTest {
    @Test
    public void testSum_WithEmptyList() {
        List<Disease> listDisease = new ArrayList<>();
        double fee = 50.0;

        double result = ServiceFee.sum(listDisease, fee);

        assertEquals(fee, result, 0.001);
    }

    @Test
    public void testSum_WithDiseases() {
        Medicine medicine = new Medicine();
        medicine.setPrice(500.0);

        Disease disease = new Disease();
        disease.setMedicine(medicine);

        List<Disease> listDisease = new ArrayList<>();

        listDisease.add(disease);
        listDisease.add(disease);
        listDisease.add(disease);
        double fee = 5000.0;

        double result = ServiceFee.sum(listDisease, fee);

        assertEquals(1500 + fee, result, 0.001);
    }
}
