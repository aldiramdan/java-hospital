package com.aldiramdan.hospital.service;

import com.aldiramdan.hospital.model.dto.request.DoctorRequest;
import com.aldiramdan.hospital.model.dto.response.ResponseData;
import com.aldiramdan.hospital.model.dto.response.ResponseDoctor;
import com.aldiramdan.hospital.model.entity.Doctor;
import com.aldiramdan.hospital.repository.DoctorRepository;
import com.aldiramdan.hospital.service.impl.DoctorServiceImpl;
import com.aldiramdan.hospital.validator.DoctorValidator;
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
public class DoctorServiceTest {
    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private DoctorValidator doctorValidator;

    @InjectMocks
    private DoctorService doctorService = new DoctorServiceImpl();

    @BeforeEach
    public void init() {
        Doctor doctor = new Doctor();
        doctor.setId("1");
        doctor.setName("John");
        doctor.setSpecialization("Dermatologist");
        doctor.setConsultationFee(71922);

        List<Doctor> listDoctor = new ArrayList<>();
        listDoctor.add(new Doctor());
        listDoctor.add(new Doctor());
        listDoctor.add(new Doctor());
        listDoctor.add(new Doctor());
        listDoctor.add(new Doctor());

        lenient().when(doctorRepository.findAll()).thenReturn(listDoctor);
        lenient().when(doctorRepository.findById("1")).thenReturn(Optional.of(doctor));
        lenient().when(doctorRepository.findByNameContaining(anyString())).thenReturn(listDoctor);
        lenient().when(doctorRepository.save(any())).thenReturn(doctor);
    }

    @Test
    public void testGetAllToDoctor_thenCorrect() {
        ResponseData responseData = doctorService.getAll();

        assertEquals(200, responseData.getCode());
        assertEquals("Success", responseData.getMessage());
        assertEquals(5, ((List) responseData.getData()).size());

        verify(doctorRepository, times(1)).findAll();
    }

    @Test
    public void testGetByIdToDoctor_thenCorrect() throws Exception {
        ResponseData responseData = doctorService.getById("1");

        assertNotNull(responseData);
        assertEquals(200, responseData.getCode());
        assertEquals("Success", responseData.getMessage());
        assertEquals("1", ((ResponseDoctor) responseData.getData()).getId());
        assertEquals("John", ((ResponseDoctor) responseData.getData()).getName());
        assertEquals("Dermatologist", ((ResponseDoctor) responseData.getData()).getSpecialization());
        assertEquals(71922, ((ResponseDoctor) responseData.getData()).getConsultationFee());

        verify(doctorRepository, times(1)).findById(anyString());
    }

    @Test
    public void testGetByNameToDoctor_thenCorrect() {
        ResponseData responseData = doctorService.getByName("John");

        assertNotNull(responseData);
        assertEquals(200, responseData.getCode());
        assertEquals("Success", responseData.getMessage());
        assertEquals(5, ((List) responseData.getData()).size());

        verify(doctorRepository, times(1)).findByNameContaining(anyString());
    }

    @Test
    public void testAddToDoctor_thenCorrect() throws Exception {
        DoctorRequest request = new DoctorRequest();
        request.setName("John");
        request.setSpecialization("Dermatologist");
        request.setConsultationFee(71922);

        ResponseData responseData = doctorService.add(request);

        assertNotNull(responseData);
        assertEquals(201, responseData.getCode());
        assertEquals("Success", responseData.getMessage());
        assertEquals("1", ((ResponseDoctor) responseData.getData()).getId());
        assertEquals("John", ((ResponseDoctor) responseData.getData()).getName());
        assertEquals("Dermatologist", ((ResponseDoctor) responseData.getData()).getSpecialization());
        assertEquals(71922, ((ResponseDoctor) responseData.getData()).getConsultationFee());

        verify(doctorRepository, times(1)).save(any());
    }

    @Test
    public void testUpdateToDoctor_thenCorrect() throws Exception {
        DoctorRequest request = new DoctorRequest();
        request.setName("Updated John");
        request.setSpecialization("Updated Dermatologist");
        request.setConsultationFee(81922);

        ResponseData responseData = doctorService.update("1", request);

        assertNotNull(responseData);
        assertEquals(200, responseData.getCode());
        assertEquals("Success", responseData.getMessage());
        assertEquals("1", ((ResponseDoctor) responseData.getData()).getId());
        assertEquals("Updated John", ((ResponseDoctor) responseData.getData()).getName());
        assertEquals("Updated Dermatologist", ((ResponseDoctor) responseData.getData()).getSpecialization());
        assertEquals(81922, ((ResponseDoctor) responseData.getData()).getConsultationFee());


        verify(doctorRepository, times(1)).findById(anyString());
        verify(doctorRepository, times(1)).save(any());
    }

    @Test
    public void testDeleteToDoctor_thenCorrect() throws Exception {
        ResponseData responseData = doctorService.delete("1");

        assertNotNull(responseData);
        assertEquals(200, responseData.getCode());
        assertEquals("Successfully deleted doctor", responseData.getMessage());

        verify(doctorRepository, times(1)).findById(anyString());
        verify(doctorRepository, times(1)).save(any());
    }

    @Test
    public void testRecoveryToDoctor_thenCorrect() throws Exception {
        ResponseData responseData = doctorService.recovery("1");

        assertNotNull(responseData);
        assertEquals(200, responseData.getCode());
        assertEquals("Successfully recovered doctor", responseData.getMessage());

        verify(doctorRepository, times(1)).findById(anyString());
        verify(doctorRepository, times(1)).save(any());
    }
}
