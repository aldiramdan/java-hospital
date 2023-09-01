package com.aldiramdan.hospital.controller;

import com.aldiramdan.hospital.exception.custom.NotFoundException;
import com.aldiramdan.hospital.exception.custom.NotProcessException;
import com.aldiramdan.hospital.model.dto.request.PatientRequest;
import com.aldiramdan.hospital.model.dto.response.ResponseData;
import com.aldiramdan.hospital.model.entity.Patient;
import com.aldiramdan.hospital.service.PatientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PatientControllerTest {
    @MockBean
    private PatientService patientService;

    @Autowired
    private PatientController patientController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void init() throws Exception {
        Patient patient = new Patient();

        List<Patient> listPatient = new ArrayList<>();
        listPatient.add(new Patient());
        listPatient.add(new Patient());

        PatientRequest request = new PatientRequest();
        request.setName("John Doe");
        request.setAge(21);
        request.setAddress("123 Main St");

        PatientRequest requestInvalid = new PatientRequest();
        requestInvalid.setName("John Doe1");
        requestInvalid.setAge(-21);
        requestInvalid.setAddress("");

        ResponseData responsePatient = new ResponseData(HttpStatus.OK.value(), "Success", patient);
        ResponseData responseAddPatient = new ResponseData(HttpStatus.CREATED.value(), "Success", patient);
        ResponseData responseListPatient = new ResponseData(HttpStatus.OK.value(), "Success", listPatient);
        ResponseData responsePatientNull = new ResponseData(HttpStatus.OK.value(), "Success", null);

        lenient().when(patientService.getAll()).thenReturn(responseListPatient);
        lenient().when(patientService.getById("1")).thenReturn(responsePatient);
        lenient().when(patientService.getById("2")).thenThrow(new NotFoundException("Patient not found"));
        lenient().when(patientService.getByName(anyString())).thenReturn(responseListPatient);
        lenient().when(patientService.add(request)).thenReturn(responseAddPatient);
        lenient().when(patientService.add(requestInvalid)).thenThrow(MethodArgumentNotValidException.class);
        lenient().when(patientService.update("1", request)).thenReturn(responsePatient);
        lenient().when(patientService.delete("1")).thenReturn(responsePatientNull);
        lenient().when(patientService.delete("2")).thenThrow(new NotProcessException("Patient is already deleted"));
        lenient().when(patientService.recovery("1")).thenReturn(responsePatientNull);
        lenient().when(patientService.recovery("2")).thenThrow(new NotProcessException("Patient is already recovered"));
    }

    @Test
    public void whenPatientControllerInjected_thenNotNull() throws Exception {
        assertThat(patientController).isNotNull();
    }

    @Test
    public void whenGetAllRequestToPatient_thenCorrectResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/patient"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isArray());

        verify(patientService, times(1)).getAll();
    }

    @Test
    public void whenGetByIdRequestToPatient_thenCorrectResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/patient/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isNotEmpty());

        verify(patientService, times(1)).getById("1");
    }

    @Test
    public void whenGetByIdRequestToPatient_thenNotFoundResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/patient/{id}", "2"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Patient not found"))
                .andExpect(jsonPath("$.error").isEmpty());

        verify(patientService, times(1)).getById("2");
    }

    @Test
    public void whenGetByNameRequestToPatient_thenCorrectResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/patient/search?name={name}", anyString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isArray());

        verify(patientService, times(1)).getByName(anyString());
    }

    @Test
    public void whenAddRequestToPatient_thenCorrectResponse() throws Exception {
        PatientRequest request = new PatientRequest();
        request.setName("John Doe");
        request.setAge(21);
        request.setAddress("123 Main St");

        mockMvc.perform(MockMvcRequestBuilders.post("/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isNotEmpty());

        verify(patientService, times(1)).add(request);
    }

    @Test
    public void whenAddRequestToPatient_thenMethodInvalidResponse() throws Exception {
        PatientRequest request = new PatientRequest();
        request.setName("John Doe1");
        request.setAge(-21);
        request.setAddress("");

        mockMvc.perform(MockMvcRequestBuilders.post("/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Error validation"))
                .andExpect(jsonPath("$.error.name").value("name must be alphabet"))
                .andExpect(jsonPath("$.error.age").value("age must be positive"))
                .andExpect(jsonPath("$.error.address").value("address is required"));
    }

    @Test
    public void whenUpdateRequestToPatient_thenCorrectResponse() throws Exception {
        PatientRequest request = new PatientRequest();
        request.setName("John Doe");
        request.setAge(21);
        request.setAddress("123 Main St");

        mockMvc.perform(MockMvcRequestBuilders.put("/patient/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isNotEmpty());

        verify(patientService, times(1)).update("1", request);
    }

    @Test
    public void whenDeleteRequestToPatient_thenCorrectResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/patient/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isEmpty());

        verify(patientService, times(1)).delete("1");
    }

    @Test
    public void whenDeleteRequestToPatient_thenIsAlreadyDeletedResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/patient/{id}", "2"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.UNPROCESSABLE_ENTITY.value()))
                .andExpect(jsonPath("$.message").value("Patient is already deleted"))
                .andExpect(jsonPath("$.error").isEmpty());

        verify(patientService, times(1)).delete("2");
    }

    @Test
    public void whenRecoveryRequestToPatient_thenCorrectResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/patient/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isEmpty());

        verify(patientService, times(1)).recovery("1");
    }

    @Test
    public void whenRecoveryRequestToPatient_thenIsAlreadyRecoveredResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/patient/{id}", "2"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.UNPROCESSABLE_ENTITY.value()))
                .andExpect(jsonPath("$.message").value("Patient is already recovered"))
                .andExpect(jsonPath("$.error").isEmpty());

        verify(patientService, times(1)).recovery("2");
    }
}

