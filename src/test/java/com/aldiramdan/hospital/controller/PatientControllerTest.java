package com.aldiramdan.hospital.controller;

import com.aldiramdan.hospital.exception.custom.NotFoundException;
import com.aldiramdan.hospital.exception.custom.NotProcessException;
import com.aldiramdan.hospital.model.dto.request.PatientRequest;
import com.aldiramdan.hospital.model.dto.response.ResponseData;
import com.aldiramdan.hospital.model.entity.Patient;
import com.aldiramdan.hospital.service.PatientService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(PatientController.class)
@AutoConfigureMockMvc
public class PatientControllerTest {
    @MockBean
    private PatientService patientService;

    @Autowired
    private PatientController patientController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenPatientControllerInjected_thenNotNull() throws Exception {
        assertThat(patientController).isNotNull();
    }

    @Test
    public void whenGetAllRequestToPatient_thenCorrectResponse() throws Exception {
        List<Patient> patients = new ArrayList<>();
        patients.add(new Patient());
        patients.add(new Patient());

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", patients);

        when(patientService.getAll()).thenReturn(responseData);

        mockMvc.perform(MockMvcRequestBuilders.get("/patient"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(responseData.getCode()))
                .andExpect(jsonPath("$.message").value(responseData.getMessage()))
                .andExpect(jsonPath("$.data").isArray());

        verify(patientService, times(1)).getAll();
    }

    @Test
    public void whenGetByIdRequestToPatient_thenCorrectResponse() throws Exception {
        UUID id = UUID.randomUUID();
        Patient patient = new Patient();
        patient.setId(id.toString());

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", patient);

        when(patientService.getById(id.toString())).thenReturn(responseData);

        mockMvc.perform(MockMvcRequestBuilders.get("/patient/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(responseData.getCode()))
                .andExpect(jsonPath("$.message").value(responseData.getMessage()))
                .andExpect(jsonPath("$.data").value(responseData.getData()));

        verify(patientService, times(1)).getById(id.toString());
    }

    @Test
    public void whenGetByIdRequestToPatient_thenNotFoundResponse() throws Exception {
        UUID id = UUID.randomUUID();
        Patient patient = new Patient();
        patient.setId(id.toString());

        when(patientService.getById(id.toString())).thenThrow(new NotFoundException("Patient not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/patient/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Patient not found"))
                .andExpect(jsonPath("$.error").isEmpty());

        verify(patientService, times(1)).getById(id.toString());
    }

    @Test
    public void whenGetByNameRequestToPatient_thenCorrectResponse() throws Exception {
        List<Patient> patients = new ArrayList<>();
        patients.add(new Patient());
        patients.add(new Patient());

        String name = "Flu";

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", patients);

        when(patientService.getByName(name)).thenReturn(responseData);

        mockMvc.perform(MockMvcRequestBuilders.get("/patient/search?name={name}", name))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(responseData.getCode()))
                .andExpect(jsonPath("$.message").value(responseData.getMessage()))
                .andExpect(jsonPath("$.data").isArray());

        verify(patientService, times(1)).getByName(name);
    }

    @Test
    public void whenAddRequestToPatient_thenCorrectResponse() throws Exception {
        PatientRequest request = new PatientRequest();
        request.setName("John Doe");
        request.setAge(21);
        request.setAddress("123 Main St");

        ResponseData responseData = new ResponseData(HttpStatus.CREATED.value(), "Success", request);

        when(patientService.add(request)).thenReturn(responseData);

        mockMvc.perform(MockMvcRequestBuilders.post("/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(responseData.getCode()))
                .andExpect(jsonPath("$.message").value(responseData.getMessage()))
                .andExpect(jsonPath("$.data").value(responseData.getData()));


        verify(patientService, times(1)).add(request);
    }

    @Test
    public void whenUpdateRequestToPatient_thenCorrectResponse() throws Exception {
        UUID id = UUID.randomUUID();
        PatientRequest request = new PatientRequest();
        request.setName("John Doe");
        request.setAge(21);
        request.setAddress("123 Main St");

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", request);

        when(patientService.update(id.toString(), request)).thenReturn(responseData);

        mockMvc.perform(MockMvcRequestBuilders.put("/patient/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(responseData.getCode()))
                .andExpect(jsonPath("$.message").value(responseData.getMessage()))
                .andExpect(jsonPath("$.data").value(responseData.getData()));


        verify(patientService, times(1)).update(id.toString(), request);
    }

    @Test
    public void whenDeleteRequestToPatient_thenCorrectResponse() throws Exception {
        UUID id = UUID.randomUUID();

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", null);

        when(patientService.delete(id.toString())).thenReturn(responseData);

        mockMvc.perform(MockMvcRequestBuilders.delete("/patient/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(responseData.getCode()))
                .andExpect(jsonPath("$.message").value(responseData.getMessage()))
                .andExpect(jsonPath("$.data").value(responseData.getData()));

        verify(patientService, times(1)).delete(id.toString());
    }

    @Test
    public void whenDeleteRequestToPatient_thenIsAlreadyDeletedResponse() throws Exception {
        UUID id = UUID.randomUUID();

        when(patientService.delete(id.toString())).thenThrow(new NotProcessException("Patient is already deleted"));

        mockMvc.perform(MockMvcRequestBuilders.delete("/patient/{id}", id))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.UNPROCESSABLE_ENTITY.value()))
                .andExpect(jsonPath("$.message").value("Patient is already deleted"))
                .andExpect(jsonPath("$.error").isEmpty());

        verify(patientService, times(1)).delete(id.toString());
    }

    @Test
    public void whenRecoveryRequestToPatient_thenCorrectResponse() throws Exception {
        UUID id = UUID.randomUUID();

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", null);

        when(patientService.recovery(id.toString())).thenReturn(responseData);

        mockMvc.perform(MockMvcRequestBuilders.patch("/patient/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(responseData.getCode()))
                .andExpect(jsonPath("$.message").value(responseData.getMessage()))
                .andExpect(jsonPath("$.data").value(responseData.getData()));

        verify(patientService, times(1)).recovery(id.toString());
    }

    @Test
    public void whenRecoveryRequestToPatient_thenIsAlreadyRecoveredResponse() throws Exception {
        UUID id = UUID.randomUUID();

        when(patientService.recovery(id.toString())).thenThrow(new NotProcessException("Patient is already recovered"));

        mockMvc.perform(MockMvcRequestBuilders.patch("/patient/{id}", id))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.UNPROCESSABLE_ENTITY.value()))
                .andExpect(jsonPath("$.message").value("Patient is already recovered"))
                .andExpect(jsonPath("$.error").isEmpty());

        verify(patientService, times(1)).recovery(id.toString());
    }
}

