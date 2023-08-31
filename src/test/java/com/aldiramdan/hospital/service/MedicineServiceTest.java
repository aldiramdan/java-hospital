package com.aldiramdan.hospital.service;

import com.aldiramdan.hospital.model.dto.request.MedicineRequest;
import com.aldiramdan.hospital.model.dto.response.ResponseData;
import com.aldiramdan.hospital.model.dto.response.ResponseMedicine;
import com.aldiramdan.hospital.model.entity.Medicine;
import com.aldiramdan.hospital.repository.MedicineRepository;
import com.aldiramdan.hospital.service.impl.MedicineServiceImpl;
import com.aldiramdan.hospital.validator.MedicineValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MedicineServiceTest {
    @Mock
    private MedicineRepository medicineRepository;

    @Mock
    private MedicineValidator medicineValidator;

    @InjectMocks
    private MedicineService medicineService = new MedicineServiceImpl();

    @BeforeEach
    public void init() {
        Medicine medicine = new Medicine();
        medicine.setId("1");
        medicine.setName("Amoxicillin");
        medicine.setPrice(3339);

        List<Medicine> listMedicine = new ArrayList<>();
        listMedicine.add(new Medicine());
        listMedicine.add(new Medicine());
        listMedicine.add(new Medicine());
        listMedicine.add(new Medicine());
        listMedicine.add(new Medicine());

        lenient().when(medicineRepository.findAll()).thenReturn(listMedicine);
        lenient().when(medicineRepository.findById("1")).thenReturn(Optional.of(medicine));
        lenient().when(medicineRepository.findByNameContaining(anyString())).thenReturn(listMedicine);
        lenient().when(medicineRepository.save(any())).thenReturn(medicine);
    }

    @Test
    public void testGetAllToMedicine_thenCorrect() {
        ResponseData responseData = medicineService.getAll();

        assertEquals(200, responseData.getCode());
        assertEquals("Success", responseData.getMessage());
        assertEquals(5, ((List) responseData.getData()).size());

        verify(medicineRepository, times(1)).findAll();
    }

    @Test
    public void testGetByIdToMedicine_thenCorrect() throws Exception {
        ResponseData responseData = medicineService.getById("1");

        assertNotNull(responseData);
        assertEquals(200, responseData.getCode());
        assertEquals("Success", responseData.getMessage());
        assertEquals("1", ((ResponseMedicine) responseData.getData()).getId());
        assertEquals("Amoxicillin", ((ResponseMedicine) responseData.getData()).getName());
        assertEquals(3339, ((ResponseMedicine) responseData.getData()).getPrice());

        verify(medicineRepository, times(1)).findById(anyString());
    }

    @Test
    public void testGetByNameToMedicine_thenCorrect() {
        ResponseData responseData = medicineService.getByName("Amoxicillin");

        assertNotNull(responseData);
        assertEquals(200, responseData.getCode());
        assertEquals("Success", responseData.getMessage());
        assertEquals(5, ((List) responseData.getData()).size());

        verify(medicineRepository, times(1)).findByNameContaining(anyString());
    }

    @Test
    public void testAddToMedicine_thenCorrect() throws Exception {
        MedicineRequest request = new MedicineRequest();
        request.setName("Amoxicillin");
        request.setPrice(3339);

        ResponseData responseData = medicineService.add(request);

        assertNotNull(responseData);
        assertEquals(201, responseData.getCode());
        assertEquals("Success", responseData.getMessage());
        assertEquals("1", ((ResponseMedicine) responseData.getData()).getId());
        assertEquals("Amoxicillin", ((ResponseMedicine) responseData.getData()).getName());
        assertEquals(3339, ((ResponseMedicine) responseData.getData()).getPrice());

        verify(medicineRepository, times(1)).save(any());
    }

    @Test
    public void testUpdateToMedicine_thenCorrect() throws Exception {
        MedicineRequest request = new MedicineRequest();
        request.setName("Updated Amoxicillin");
        request.setPrice(4339);

        ResponseData responseData = medicineService.update("1", request);

        assertNotNull(responseData);
        assertEquals(200, responseData.getCode());
        assertEquals("Success", responseData.getMessage());
        assertEquals("1", ((ResponseMedicine) responseData.getData()).getId());
        assertEquals("Updated Amoxicillin", ((ResponseMedicine) responseData.getData()).getName());
        assertEquals(4339, ((ResponseMedicine) responseData.getData()).getPrice());


        verify(medicineRepository, times(1)).findById(anyString());
        verify(medicineRepository, times(1)).save(any());
    }

    @Test
    public void testDeleteToMedicine_thenCorrect() throws Exception {
        ResponseData responseData = medicineService.delete("1");

        assertNotNull(responseData);
        assertEquals(200, responseData.getCode());
        assertEquals("Successfully deleted medicine", responseData.getMessage());

        verify(medicineRepository, times(1)).findById(anyString());
        verify(medicineRepository, times(1)).save(any());
    }

    @Test
    public void testRecoveryToMedicine_thenCorrect() throws Exception {
        ResponseData responseData = medicineService.recovery("1");

        assertNotNull(responseData);
        assertEquals(200, responseData.getCode());
        assertEquals("Successfully recovered medicine", responseData.getMessage());

        verify(medicineRepository, times(1)).findById(anyString());
        verify(medicineRepository, times(1)).save(any());
    }
}
