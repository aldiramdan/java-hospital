package com.aldiramdan.hospital.controller;

import com.aldiramdan.hospital.exception.custom.NotFoundException;
import com.aldiramdan.hospital.exception.custom.NotProcessException;
import com.aldiramdan.hospital.model.dto.request.DiseaseRequest;
import com.aldiramdan.hospital.model.dto.request.TreatmentRequest;
import com.aldiramdan.hospital.model.dto.response.ResponseData;
import com.aldiramdan.hospital.model.entity.Treatment;
import com.aldiramdan.hospital.service.TreatmentService;
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
public class TreatmentControllerTest {
    @MockBean
    private TreatmentService treatmentService;

    @Autowired
    private TreatmentController treatmentController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void init() throws Exception {
        Treatment treatment = new Treatment();

        List<Treatment> listTreatment = new ArrayList<>();
        listTreatment.add(new Treatment());
        listTreatment.add(new Treatment());

        List<DiseaseRequest> diseases = new ArrayList<>();
        diseases.add(new DiseaseRequest());
        diseases.add(new DiseaseRequest());

        TreatmentRequest request = new TreatmentRequest();
        request.setPatientId("6b3bf6cb-e060-4062-a9ed-f39eb22761f7");
        request.setDoctorId("1e96cdba-5eb8-4df3-b2b4-54ffb3b165ac");
        request.setDisease(diseases);

        String patientId = "wrong id doctor";
        String doctorId = "wrong id patient";
        List<DiseaseRequest> diseasesInvalid = new ArrayList<>();

        TreatmentRequest requestInvalid = new TreatmentRequest();
        requestInvalid.setPatientId(patientId);
        requestInvalid.setDoctorId(doctorId);
        requestInvalid.setDisease(diseasesInvalid);

        ResponseData responseTreatment = new ResponseData(HttpStatus.OK.value(), "Success", treatment);
        ResponseData responseAddTreatment = new ResponseData(HttpStatus.CREATED.value(), "Success", treatment);
        ResponseData responseListTreatment = new ResponseData(HttpStatus.OK.value(), "Success", listTreatment);
        ResponseData responseTreatmentNull = new ResponseData(HttpStatus.OK.value(), "Success", null);

        lenient().when(treatmentService.getAll()).thenReturn(responseListTreatment);
        lenient().when(treatmentService.getById("1")).thenReturn(responseTreatment);
        lenient().when(treatmentService.getById("2")).thenThrow(new NotFoundException("Treatment not found"));
        lenient().when(treatmentService.getByPatientName(anyString())).thenReturn(responseListTreatment);
        lenient().when(treatmentService.add(request)).thenReturn(responseAddTreatment);
        lenient().when(treatmentService.add(requestInvalid)).thenThrow(MethodArgumentNotValidException.class);
        lenient().when(treatmentService.update("1", request)).thenReturn(responseTreatment);
        lenient().when(treatmentService.delete("1")).thenReturn(responseTreatmentNull);
        lenient().when(treatmentService.delete("2")).thenThrow(new NotProcessException("Treatment is already deleted"));
        lenient().when(treatmentService.recovery("1")).thenReturn(responseTreatmentNull);
        lenient().when(treatmentService.recovery("2")).thenThrow(new NotProcessException("Treatment is already recovered"));
    }

    @Test
    public void whenTreatmentControllerInjected_thenNotNull() throws Exception {
        assertThat(treatmentController).isNotNull();
    }

    @Test
    public void whenGetAllRequestToTreatment_thenCorrectResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/treatment"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isArray());

        verify(treatmentService, times(1)).getAll();
    }

    @Test
    public void whenGetByIdRequestToTreatment_thenCorrectResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/treatment/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isNotEmpty());

        verify(treatmentService, times(1)).getById("1");
    }

    @Test
    public void whenGetByIdRequestToTreatment_thenNotFoundResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/treatment/{id}", "2"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Treatment not found"))
                .andExpect(jsonPath("$.error").isEmpty());

        verify(treatmentService, times(1)).getById("2");
    }

    @Test
    public void whenGetByNameRequestToTreatment_thenCorrectResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/treatment/search?name={name}", anyString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isArray());

        verify(treatmentService, times(1)).getByPatientName(anyString());
    }

    @Test
    public void whenAddRequestToTreatment_thenCorrectResponse() throws Exception {
        List<DiseaseRequest> diseases = new ArrayList<>();
        diseases.add(new DiseaseRequest());
        diseases.add(new DiseaseRequest());

        TreatmentRequest request = new TreatmentRequest();
        request.setPatientId("6b3bf6cb-e060-4062-a9ed-f39eb22761f7");
        request.setDoctorId("1e96cdba-5eb8-4df3-b2b4-54ffb3b165ac");
        request.setDisease(diseases);

        mockMvc.perform(MockMvcRequestBuilders.post("/treatment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isNotEmpty());

        verify(treatmentService, times(1)).add(request);
    }

    @Test
    public void whenAddRequestToTreatment_thenFailureResponse() throws Exception {
        List<DiseaseRequest> diseases = new ArrayList<>();

        TreatmentRequest request = new TreatmentRequest();
        request.setPatientId("wrong id patient");
        request.setDoctorId("wrong id doctor");
        request.setDisease(diseases);

        mockMvc.perform(MockMvcRequestBuilders.post("/treatment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Error validation"))
                .andExpect(jsonPath("$.error.disease").value("must not be empty"))
                .andExpect(jsonPath("$.error.patientId").value("patient must be uuid"))
                .andExpect(jsonPath("$.error.doctorId").value("doctor must be uuid"));
    }

    @Test
    public void whenUpdateRequestToTreatment_thenCorrectResponse() throws Exception {
        List<DiseaseRequest> diseases = new ArrayList<>();
        diseases.add(new DiseaseRequest());
        diseases.add(new DiseaseRequest());

        TreatmentRequest request = new TreatmentRequest();
        request.setPatientId("6b3bf6cb-e060-4062-a9ed-f39eb22761f7");
        request.setDoctorId("1e96cdba-5eb8-4df3-b2b4-54ffb3b165ac");
        request.setDisease(diseases);

        mockMvc.perform(MockMvcRequestBuilders.put("/treatment/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isNotEmpty());

        verify(treatmentService, times(1)).update("1", request);
    }

    @Test
    public void whenDeleteRequestToTreatment_thenCorrectResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/treatment/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isEmpty());

        verify(treatmentService, times(1)).delete("1");
    }

    @Test
    public void whenDeleteRequestToTreatment_thenIsAlreadyDeletedResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/treatment/{id}", "2"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.UNPROCESSABLE_ENTITY.value()))
                .andExpect(jsonPath("$.message").value("Treatment is already deleted"))
                .andExpect(jsonPath("$.error").isEmpty());

        verify(treatmentService, times(1)).delete("2");
    }

    @Test
    public void whenRecoveryRequestToTreatment_thenCorrectResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/treatment/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isEmpty());

        verify(treatmentService, times(1)).recovery("1");
    }

    @Test
    public void whenRecoveryRequestToTreatment_thenIsAlreadyRecoveredResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/treatment/{id}", "2"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.UNPROCESSABLE_ENTITY.value()))
                .andExpect(jsonPath("$.message").value("Treatment is already recovered"))
                .andExpect(jsonPath("$.error").isEmpty());

        verify(treatmentService, times(1)).recovery("2");
    }
}
