package com.aldiramdan.hospital.controller;

import com.aldiramdan.hospital.exception.custom.NotFoundException;
import com.aldiramdan.hospital.exception.custom.NotProcessException;
import com.aldiramdan.hospital.model.dto.request.DoctorRequest;
import com.aldiramdan.hospital.model.dto.response.ResponseData;
import com.aldiramdan.hospital.model.entity.Doctor;
import com.aldiramdan.hospital.service.DoctorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(DoctorController.class)
@AutoConfigureMockMvc
public class DoctorControllerTest {
    @MockBean
    private DoctorService doctorService;

    @Autowired
    private DoctorController doctorController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenDoctorControllerInjected_thenNotNull() throws Exception {
        assertThat(doctorController).isNotNull();
    }

    @Test
    public void whenGetAllRequestToDoctor_thenCorrectResponse() throws Exception {
        List<Doctor> doctors = new ArrayList<>();
        doctors.add(new Doctor());
        doctors.add(new Doctor());

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", doctors);

        when(doctorService.getAll()).thenReturn(responseData);

        mockMvc.perform(MockMvcRequestBuilders.get("/doctor"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(responseData.getCode()))
                .andExpect(jsonPath("$.message").value(responseData.getMessage()))
                .andExpect(jsonPath("$.data").isArray());

        verify(doctorService, times(1)).getAll();
    }

    @Test
    public void whenGetByIdRequestToDoctor_thenCorrectResponse() throws Exception {
        UUID id = UUID.randomUUID();
        Doctor doctor = new Doctor();
        doctor.setId(id.toString());

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", doctor);

        when(doctorService.getById(id.toString())).thenReturn(responseData);

        mockMvc.perform(MockMvcRequestBuilders.get("/doctor/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(responseData.getCode()))
                .andExpect(jsonPath("$.message").value(responseData.getMessage()))
                .andExpect(jsonPath("$.data").value(responseData.getData()));

        verify(doctorService, times(1)).getById(id.toString());
    }

    @Test
    public void whenGetByIdRequestToDoctor_thenNotFoundResponse() throws Exception {
        UUID id = UUID.randomUUID();
        Doctor patient = new Doctor();
        patient.setId(id.toString());

        when(doctorService.getById(id.toString())).thenThrow(new NotFoundException("Doctor not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/doctor/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Doctor not found"))
                .andExpect(jsonPath("$.error").isEmpty());

        verify(doctorService, times(1)).getById(id.toString());
    }

    @Test
    public void whenGetByNameRequestToDoctor_thenCorrectResponse() throws Exception {
        List<Doctor> doctors = new ArrayList<>();
        doctors.add(new Doctor());
        doctors.add(new Doctor());

        String name = "john";

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", doctors);

        when(doctorService.getByName(name)).thenReturn(responseData);

        mockMvc.perform(MockMvcRequestBuilders.get("/doctor/search?name={name}", name))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(responseData.getCode()))
                .andExpect(jsonPath("$.message").value(responseData.getMessage()))
                .andExpect(jsonPath("$.data").isArray());

        verify(doctorService, times(1)).getByName(name);
    }

    @Test
    public void whenAddRequestToDoctor_thenCorrectResponse() throws Exception {
        DoctorRequest request = new DoctorRequest();
        request.setName("Dr. John Smith");
        request.setSpecialization("Dermatologist");
        request.setConsultationFee(71922);

        ResponseData responseData = new ResponseData(HttpStatus.CREATED.value(), "Success", request);

        when(doctorService.add(request)).thenReturn(responseData);

        mockMvc.perform(MockMvcRequestBuilders.post("/doctor", request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(responseData.getCode()))
                .andExpect(jsonPath("$.message").value(responseData.getMessage()))
                .andExpect(jsonPath("$.data").value(responseData.getData()));

        verify(doctorService, times(1)).add(request);
    }

    @Test
    public void whenAddRequestToDoctor_thenFailureResponse() throws Exception {
        DoctorRequest request = new DoctorRequest();
        request.setName("Dr. John Smith1");
        request.setSpecialization("Dermatologist1");
        request.setConsultationFee(-71922);

        when(doctorService.add(request)).thenThrow(MethodArgumentNotValidException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Error validation"))
                .andExpect(jsonPath("$.error.name").value("name must be alphabet"))
                .andExpect(jsonPath("$.error.specialization").value("specialization must be alphabet"))
                .andExpect(jsonPath("$.error.consultationFee").value("consultationFee must be positive"));
    }

    @Test
    public void whenUpdateRequestToDoctor_thenCorrectResponse() throws Exception {
        UUID id = UUID.randomUUID();
        DoctorRequest request = new DoctorRequest();
        request.setName("Dr. John Smith");
        request.setSpecialization("Dermatologist");
        request.setConsultationFee(71922);

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", request);

        when(doctorService.update(id.toString(), request)).thenReturn(responseData);

        mockMvc.perform(MockMvcRequestBuilders.put("/doctor/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(responseData.getCode()))
                .andExpect(jsonPath("$.message").value(responseData.getMessage()))
                .andExpect(jsonPath("$.data").value(responseData.getData()));

        verify(doctorService, times(1)).update(id.toString(), request);
    }

    @Test
    public void whenDeleteRequestToDoctor_thenCorrectResponse() throws Exception {
        UUID id = UUID.randomUUID();

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", null);

        when(doctorService.delete(id.toString())).thenReturn(responseData);

        mockMvc.perform(MockMvcRequestBuilders.delete("/doctor/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(responseData.getCode()))
                .andExpect(jsonPath("$.message").value(responseData.getMessage()))
                .andExpect(jsonPath("$.data").value(responseData.getData()));

        verify(doctorService, times(1)).delete(id.toString());
    }

    @Test
    public void whenDeleteRequestToDoctor_thenIsAlreadyDeletedResponse() throws Exception {
        UUID id = UUID.randomUUID();

        when(doctorService.delete(id.toString())).thenThrow(new NotProcessException("Doctor is already deleted"));

        mockMvc.perform(MockMvcRequestBuilders.delete("/doctor/{id}", id))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.UNPROCESSABLE_ENTITY.value()))
                .andExpect(jsonPath("$.message").value("Doctor is already deleted"))
                .andExpect(jsonPath("$.error").isEmpty());

        verify(doctorService, times(1)).delete(id.toString());
    }

    @Test
    public void whenRecoveryRequestToDoctor_thenCorrectResponse() throws Exception {
        UUID id = UUID.randomUUID();

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", null);

        when(doctorService.recovery(id.toString())).thenReturn(responseData);

        mockMvc.perform(MockMvcRequestBuilders.patch("/doctor/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(responseData.getCode()))
                .andExpect(jsonPath("$.message").value(responseData.getMessage()))
                .andExpect(jsonPath("$.data").value(responseData.getData()));

        verify(doctorService, times(1)).recovery(id.toString());
    }

    @Test
    public void whenRecoveryRequestToDoctor_thenIsAlreadyRecoveredResponse() throws Exception {
        UUID id = UUID.randomUUID();

        when(doctorService.recovery(id.toString())).thenThrow(new NotProcessException("Doctor is already recovered"));

        mockMvc.perform(MockMvcRequestBuilders.patch("/doctor/{id}", id))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.UNPROCESSABLE_ENTITY.value()))
                .andExpect(jsonPath("$.message").value("Doctor is already recovered"))
                .andExpect(jsonPath("$.error").isEmpty());

        verify(doctorService, times(1)).recovery(id.toString());
    }
}
