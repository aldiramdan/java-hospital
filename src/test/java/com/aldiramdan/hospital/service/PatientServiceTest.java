package com.aldiramdan.hospital.service;

import com.aldiramdan.hospital.model.dto.request.PatientRequest;
import com.aldiramdan.hospital.model.dto.response.ResponseData;
import com.aldiramdan.hospital.model.dto.response.ResponsePatient;
import com.aldiramdan.hospital.model.entity.Patient;
import com.aldiramdan.hospital.repository.PatientRepository;
import com.aldiramdan.hospital.service.impl.PatientServiceImpl;
import com.aldiramdan.hospital.validator.PatientValidator;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {
    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientValidator patientValidator;

    @InjectMocks
    private PatientService patientService = new PatientServiceImpl();

    @BeforeEach
    public void init() {
        Patient patient = new Patient();
        patient.setId("1");
        patient.setName("John");
        patient.setAge(30);
        patient.setAddress("123 Main St");

        List<Patient> listPatient = new ArrayList<>();
        listPatient.add(new Patient());
        listPatient.add(new Patient());
        listPatient.add(new Patient());
        listPatient.add(new Patient());
        listPatient.add(new Patient());

        lenient().when(patientRepository.findAll()).thenReturn(listPatient);
        lenient().when(patientRepository.findById("1")).thenReturn(Optional.of(patient));
        lenient().when(patientRepository.findByNameContaining(anyString())).thenReturn(listPatient);
        lenient().when(patientRepository.save(any())).thenReturn(patient);
    }

    @Test
    public void testGetAllToPatient_thenCorrect() {
        ResponseData responseData = patientService.getAll();

        assertEquals(200, responseData.getCode());
        assertEquals("Success", responseData.getMessage());
        assertEquals(5, ((List) responseData.getData()).size());

        verify(patientRepository, times(1)).findAll();
    }

    @Test
    public void testGetByIdToPatient_thenCorrect() throws Exception {
        ResponseData responseData = patientService.getById("1");

        assertNotNull(responseData);
        assertEquals(200, responseData.getCode());
        assertEquals("Success", responseData.getMessage());
        assertEquals("1", ((ResponsePatient) responseData.getData()).getId());
        assertEquals("John", ((ResponsePatient) responseData.getData()).getName());
        assertEquals(30, ((ResponsePatient) responseData.getData()).getAge());
        assertEquals("123 Main St", ((ResponsePatient) responseData.getData()).getAddress());

        verify(patientRepository, times(1)).findById(anyString());
    }

    @Test
    public void testGetByNameToPatient_thenCorrect() {
        ResponseData responseData = patientService.getByName("John");

        assertNotNull(responseData);
        assertEquals(200, responseData.getCode());
        assertEquals("Success", responseData.getMessage());
        assertEquals(5, ((List) responseData.getData()).size());

        verify(patientRepository, times(1)).findByNameContaining(anyString());
    }

    @Test
    public void testAddToPatient_thenCorrect() throws Exception {
        PatientRequest request = new PatientRequest();
        request.setName("John");
        request.setAge(30);
        request.setAddress("123 Main St");

        ResponseData responseData = patientService.add(request);

        assertNotNull(responseData);
        assertEquals(201, responseData.getCode());
        assertEquals("Success", responseData.getMessage());
        assertEquals("1", ((ResponsePatient) responseData.getData()).getId());
        assertEquals("John", ((ResponsePatient) responseData.getData()).getName());
        assertEquals(30, ((ResponsePatient) responseData.getData()).getAge());
        assertEquals("123 Main St", ((ResponsePatient) responseData.getData()).getAddress());

        verify(patientRepository, times(1)).save(any());
    }

    @Test
    public void testUpdateToPatient_thenCorrect() throws Exception {
        PatientRequest request = new PatientRequest();
        request.setName("Updated John");
        request.setAge(35);
        request.setAddress("456 Updated St");

        ResponseData responseData = patientService.update("1", request);

        assertNotNull(responseData);
        assertEquals(200, responseData.getCode());
        assertEquals("Success", responseData.getMessage());
        assertEquals("1", ((ResponsePatient) responseData.getData()).getId());
        assertEquals("Updated John", ((ResponsePatient) responseData.getData()).getName());
        assertEquals(35, ((ResponsePatient) responseData.getData()).getAge());
        assertEquals("456 Updated St", ((ResponsePatient) responseData.getData()).getAddress());


        verify(patientRepository, times(1)).findById(anyString());
        verify(patientRepository, times(1)).save(any());
    }

    @Test
    public void testDeleteToPatient_thenCorrect() throws Exception {
        ResponseData responseData = patientService.delete("1");

        assertNotNull(responseData);
        assertEquals(200, responseData.getCode());
        assertEquals("Successfully deleted patient", responseData.getMessage());

        verify(patientRepository, times(1)).findById(anyString());
        verify(patientRepository, times(1)).save(any());
    }

    @Test
    public void testRecoveryToPatient_thenCorrect() throws Exception {
        ResponseData responseData = patientService.recovery("1");

        assertNotNull(responseData);
        assertEquals(200, responseData.getCode());
        assertEquals("Successfully recovered patient", responseData.getMessage());

        verify(patientRepository, times(1)).findById(anyString());
        verify(patientRepository, times(1)).save(any());
    }
}
