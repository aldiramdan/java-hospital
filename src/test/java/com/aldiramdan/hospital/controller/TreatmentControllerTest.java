package com.aldiramdan.hospital.controller;

import com.aldiramdan.hospital.model.dto.request.DiseaseRequest;
import com.aldiramdan.hospital.model.dto.request.TreatmentRequest;
import com.aldiramdan.hospital.model.dto.response.ResponseData;
import com.aldiramdan.hospital.model.entity.Treatment;
import com.aldiramdan.hospital.service.TreatmentService;
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
@WebMvcTest(TreatmentController.class)
@AutoConfigureMockMvc
public class TreatmentControllerTest {
    @MockBean
    private TreatmentService treatmentService;

    @Autowired
    private TreatmentController treatmentController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenTreatmentControllerInjected_thenNotNull() throws Exception {
        assertThat(treatmentController).isNotNull();
    }

    @Test
    public void whenGetAllRequestToTreatment_thenCorrectResponse() throws Exception {
        List<Treatment> treatments = new ArrayList<>();
        treatments.add(new Treatment());
        treatments.add(new Treatment());

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", treatments);

        when(treatmentService.getAll()).thenReturn(responseData);

        mockMvc.perform(MockMvcRequestBuilders.get("/treatment"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(responseData.getCode()))
                .andExpect(jsonPath("$.message").value(responseData.getMessage()))
                .andExpect(jsonPath("$.data").isArray());

        verify(treatmentService, times(1)).getAll();
    }

    @Test
    public void whenGetByIdRequestToTreatment_thenCorrectResponse() throws Exception {
        UUID id = UUID.randomUUID();
        Treatment treatment = new Treatment();
        treatment.setId(id.toString());

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", treatment);

        when(treatmentService.getById(id.toString())).thenReturn(responseData);

        mockMvc.perform(MockMvcRequestBuilders.get("/treatment/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(responseData.getCode()))
                .andExpect(jsonPath("$.message").value(responseData.getMessage()))
                .andExpect(jsonPath("$.data").value(responseData.getData()));

        verify(treatmentService, times(1)).getById(id.toString());
    }

    @Test
    public void whenGetByNameRequestToTreatment_thenCorrectResponse() throws Exception {
        List<Treatment> treatments = new ArrayList<>();
        treatments.add(new Treatment());
        treatments.add(new Treatment());

        String name = "John";

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", treatments);

        when(treatmentService.getByPatientName(name)).thenReturn(responseData);

        mockMvc.perform(MockMvcRequestBuilders.get("/treatment/search?name={name}", name))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(responseData.getCode()))
                .andExpect(jsonPath("$.message").value(responseData.getMessage()))
                .andExpect(jsonPath("$.data").isArray());

        verify(treatmentService, times(1)).getByPatientName(name);
    }

    @Test
    public void whenAddRequestToTreatment_thenCorrectResponse() throws Exception {
        UUID patientId = UUID.randomUUID();
        UUID doctorId = UUID.randomUUID();
        List<DiseaseRequest> diseases = new ArrayList<>();
        diseases.add(new DiseaseRequest());
        diseases.add(new DiseaseRequest());

        TreatmentRequest request = new TreatmentRequest();
        request.setPatientId(patientId.toString());
        request.setDoctorId(doctorId.toString());
        request.setDisease(diseases);

        ResponseData responseData = new ResponseData(HttpStatus.CREATED.value(), "Success", request);

        when(treatmentService.add(request)).thenReturn(responseData);

        mockMvc.perform(MockMvcRequestBuilders.post("/treatment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(responseData.getCode()))
                .andExpect(jsonPath("$.message").value(responseData.getMessage()))
                .andExpect(jsonPath("$.data").value(responseData.getData()));


        verify(treatmentService, times(1)).add(request);
    }

    @Test
    public void whenUpdateRequestToTreatment_thenCorrectResponse() throws Exception {
        UUID id = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();
        UUID doctorId = UUID.randomUUID();
        List<DiseaseRequest> diseases = new ArrayList<>();
        diseases.add(new DiseaseRequest());
        diseases.add(new DiseaseRequest());

        TreatmentRequest request = new TreatmentRequest();
        request.setPatientId(patientId.toString());
        request.setDoctorId(doctorId.toString());
        request.setDisease(diseases);

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", request);

        when(treatmentService.update(id.toString(), request)).thenReturn(responseData);

        mockMvc.perform(MockMvcRequestBuilders.put("/treatment/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(responseData.getCode()))
                .andExpect(jsonPath("$.message").value(responseData.getMessage()))
                .andExpect(jsonPath("$.data").value(responseData.getData()));


        verify(treatmentService, times(1)).update(id.toString(), request);
    }

    @Test
    public void whenDeleteRequestToTreatment_thenCorrectResponse() throws Exception {
        UUID id = UUID.randomUUID();

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", null);

        when(treatmentService.delete(id.toString())).thenReturn(responseData);

        mockMvc.perform(MockMvcRequestBuilders.delete("/treatment/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(responseData.getCode()))
                .andExpect(jsonPath("$.message").value(responseData.getMessage()))
                .andExpect(jsonPath("$.data").value(responseData.getData()));

        verify(treatmentService, times(1)).delete(id.toString());
    }

    @Test
    public void whenRecoveryRequestToTreatment_thenCorrectResponse() throws Exception {
        UUID id = UUID.randomUUID();

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", null);

        when(treatmentService.recovery(id.toString())).thenReturn(responseData);

        mockMvc.perform(MockMvcRequestBuilders.patch("/treatment/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(responseData.getCode()))
                .andExpect(jsonPath("$.message").value(responseData.getMessage()))
                .andExpect(jsonPath("$.data").value(responseData.getData()));

        verify(treatmentService, times(1)).recovery(id.toString());
    }
}
