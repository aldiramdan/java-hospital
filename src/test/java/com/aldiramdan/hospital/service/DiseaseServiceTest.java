package com.aldiramdan.hospital.service;

import com.aldiramdan.hospital.model.dto.request.DiseaseRequest;
import com.aldiramdan.hospital.model.dto.response.ResponseData;
import com.aldiramdan.hospital.model.dto.response.ResponseDisease;
import com.aldiramdan.hospital.model.entity.Disease;
import com.aldiramdan.hospital.model.entity.Medicine;
import com.aldiramdan.hospital.repository.DiseaseRepository;
import com.aldiramdan.hospital.repository.MedicineRepository;
import com.aldiramdan.hospital.service.impl.DiseaseServiceImpl;
import com.aldiramdan.hospital.validator.DiseaseValidator;
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
public class DiseaseServiceTest {
    @Mock
    private DiseaseRepository diseaseRepository;

    @Mock
    private DiseaseValidator diseaseValidator;

    @Mock
    private MedicineRepository medicineRepository;

    @Mock
    private MedicineValidator medicineValidator;

    @InjectMocks
    private DiseaseService diseaseService = new DiseaseServiceImpl();

    @BeforeEach
    public void init() {
        Medicine medicine = new Medicine();
        medicine.setId("1");
        medicine.setName("Amoxicillin");
        medicine.setPrice(3339);

        Disease disease = new Disease();
        disease.setId("1");
        disease.setName("Allergies");
        disease.setMedicine(medicine);

        List<Disease> listDisease = new ArrayList<>();
        listDisease.add(disease);
        listDisease.add(disease);
        listDisease.add(disease);
        listDisease.add(disease);
        listDisease.add(disease);

        lenient().when(diseaseRepository.findAll()).thenReturn(listDisease);
        lenient().when(diseaseRepository.findById("1")).thenReturn(Optional.of(disease));
        lenient().when(diseaseRepository.findByNameContaining(anyString())).thenReturn(listDisease);
        lenient().when(diseaseRepository.save(any())).thenReturn(disease);

        lenient().when(medicineRepository.findByName(anyString())).thenReturn(Optional.of(medicine));
    }

    @Test
    public void testGetAllToDisease_thenCorrect() {
        ResponseData responseData = diseaseService.getAll();

        assertEquals(200, responseData.getCode());
        assertEquals("Success", responseData.getMessage());
        assertEquals(5, ((List) responseData.getData()).size());

        verify(diseaseRepository, times(1)).findAll();
    }

    @Test
    public void testGetByIdToDisease_thenCorrect() throws Exception {
        ResponseData responseData = diseaseService.getById("1");

        assertNotNull(responseData);
        assertEquals(200, responseData.getCode());
        assertEquals("Success", responseData.getMessage());
        assertEquals("1", ((ResponseDisease) responseData.getData()).getId());
        assertEquals("Allergies", ((ResponseDisease) responseData.getData()).getName());
        assertEquals("1", ((ResponseDisease) responseData.getData()).getMedicine().getId());
        assertEquals("Amoxicillin", ((ResponseDisease) responseData.getData()).getMedicine().getName());
        assertEquals(3339, ((ResponseDisease) responseData.getData()).getMedicine().getPrice());

        verify(diseaseRepository, times(1)).findById(anyString());
    }

    @Test
    public void testGetByNameToDisease_thenCorrect() {
        ResponseData responseData = diseaseService.getByName("Flu");

        assertNotNull(responseData);
        assertEquals(200, responseData.getCode());
        assertEquals("Success", responseData.getMessage());
        assertEquals(5, ((List) responseData.getData()).size());

        verify(diseaseRepository, times(1)).findByNameContaining(anyString());
    }

    @Test
    public void testAddToDisease_thenCorrect() throws Exception {
        DiseaseRequest request = new DiseaseRequest();
        request.setName("Allergies");
        request.setMedicine("Amoxicillin");

        ResponseData responseData = diseaseService.add(request);

        assertNotNull(responseData);
        assertEquals(201, responseData.getCode());
        assertEquals("Success", responseData.getMessage());
        assertEquals("1", ((ResponseDisease) responseData.getData()).getId());
        assertEquals("Allergies", ((ResponseDisease) responseData.getData()).getName());
        assertEquals("1", ((ResponseDisease) responseData.getData()).getMedicine().getId());
        assertEquals("Amoxicillin", ((ResponseDisease) responseData.getData()).getMedicine().getName());
        assertEquals(3339, ((ResponseDisease) responseData.getData()).getMedicine().getPrice());

        verify(medicineRepository, times(1)).findByName(anyString());
        verify(diseaseRepository, times(1)).save(any());
    }

    @Test
    public void testUpdateToDisease_thenCorrect() throws Exception {
        DiseaseRequest request = new DiseaseRequest();
        request.setName("Updated Allergies");
        request.setMedicine("Paracetamol");

        ResponseData responseData = diseaseService.update("1", request);

        assertNotNull(responseData);
        assertEquals(200, responseData.getCode());
        assertEquals("Success", responseData.getMessage());
        assertEquals("1", ((ResponseDisease) responseData.getData()).getId());
        assertEquals("Updated Allergies", ((ResponseDisease) responseData.getData()).getName());
        assertEquals("1", ((ResponseDisease) responseData.getData()).getMedicine().getId());
        assertEquals("Amoxicillin", ((ResponseDisease) responseData.getData()).getMedicine().getName());
        assertEquals(3339, ((ResponseDisease) responseData.getData()).getMedicine().getPrice());

        verify(diseaseRepository, times(1)).findById(anyString());
        verify(medicineRepository, times(1)).findByName(anyString());
        verify(diseaseRepository, times(1)).save(any());
    }

    @Test
    public void testDeleteToDisease_thenCorrect() throws Exception {
        ResponseData responseData = diseaseService.delete("1");

        assertNotNull(responseData);
        assertEquals(200, responseData.getCode());
        assertEquals("Successfully deleted disease", responseData.getMessage());

        verify(diseaseRepository, times(1)).findById(anyString());
        verify(diseaseRepository, times(1)).save(any());
    }

    @Test
    public void testRecoveryToDisease_thenCorrect() throws Exception {
        ResponseData responseData = diseaseService.recovery("1");

        assertNotNull(responseData);
        assertEquals(200, responseData.getCode());
        assertEquals("Successfully recovered disease", responseData.getMessage());

        verify(diseaseRepository, times(1)).findById(anyString());
        verify(diseaseRepository, times(1)).save(any());
    }
}
