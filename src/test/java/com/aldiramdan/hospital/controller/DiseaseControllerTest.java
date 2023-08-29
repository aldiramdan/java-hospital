package com.aldiramdan.hospital.controller;

import com.aldiramdan.hospital.exception.custom.NotFoundException;
import com.aldiramdan.hospital.exception.custom.NotProcessException;
import com.aldiramdan.hospital.model.dto.request.DiseaseRequest;
import com.aldiramdan.hospital.model.dto.response.ResponseData;
import com.aldiramdan.hospital.model.entity.Disease;
import com.aldiramdan.hospital.service.DiseaseService;
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
@WebMvcTest(DiseaseController.class)
@AutoConfigureMockMvc
public class DiseaseControllerTest {
    @MockBean
    private DiseaseService diseaseService;

    @Autowired
    private DiseaseController diseaseController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenDiseaseControllerInjected_thenNotNull() throws Exception {
        assertThat(diseaseController).isNotNull();
    }

    @Test
    public void whenGetAllRequestToDisease_thenCorrectResponse() throws Exception {
        List<Disease> diseases = new ArrayList<>();
        diseases.add(new Disease());
        diseases.add(new Disease());

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", diseases);

        when(diseaseService.getAll()).thenReturn(responseData);

        mockMvc.perform(MockMvcRequestBuilders.get("/disease"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(responseData.getCode()))
                .andExpect(jsonPath("$.message").value(responseData.getMessage()))
                .andExpect(jsonPath("$.data").isArray());

        verify(diseaseService, times(1)).getAll();
    }

    @Test
    public void whenGetByIdRequestToDisease_thenCorrectResponse() throws Exception {
        UUID id = UUID.randomUUID();
        Disease disease = new Disease();
        disease.setId(id.toString());

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", disease);

        when(diseaseService.getById(id.toString())).thenReturn(responseData);

        mockMvc.perform(MockMvcRequestBuilders.get("/disease/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(responseData.getCode()))
                .andExpect(jsonPath("$.message").value(responseData.getMessage()))
                .andExpect(jsonPath("$.data").value(responseData.getData()));

        verify(diseaseService, times(1)).getById(id.toString());
    }

    @Test
    public void whenGetByIdRequestToDisease_thenNotFoundResponse() throws Exception {
        UUID id = UUID.randomUUID();
        Disease patient = new Disease();
        patient.setId(id.toString());

        when(diseaseService.getById(id.toString())).thenThrow(new NotFoundException("Disease not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/disease/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Disease not found"))
                .andExpect(jsonPath("$.error").isEmpty());

        verify(diseaseService, times(1)).getById(id.toString());
    }

    @Test
    public void whenGetByNameRequestToDisease_thenCorrectResponse() throws Exception {
        List<Disease> diseases = new ArrayList<>();
        diseases.add(new Disease());
        diseases.add(new Disease());

        String name = "flu";

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", diseases);

        when(diseaseService.getByName(name)).thenReturn(responseData);

        mockMvc.perform(MockMvcRequestBuilders.get("/disease/search?name={name}", name))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(responseData.getCode()))
                .andExpect(jsonPath("$.message").value(responseData.getMessage()))
                .andExpect(jsonPath("$.data").isArray());

        verify(diseaseService, times(1)).getByName(name);
    }

    @Test
    public void whenAddRequestToDisease_thenCorrectResponse() throws Exception {
        DiseaseRequest request = new DiseaseRequest();
        request.setName("Flu");
        request.setMedicine("Paracetamol");

        ResponseData responseData = new ResponseData(HttpStatus.CREATED.value(), "Success", request);

        when(diseaseService.add(request)).thenReturn(responseData);

        mockMvc.perform(MockMvcRequestBuilders.post("/disease")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(responseData.getCode()))
                .andExpect(jsonPath("$.message").value(responseData.getMessage()))
                .andExpect(jsonPath("$.data").value(responseData.getData()));

        verify(diseaseService, times(1)).add(request);
    }

    @Test
    public void whenAddRequestToDisease_thenFailureResponse() throws Exception {
        DiseaseRequest request = new DiseaseRequest();
        request.setName("Flu1");
        request.setMedicine("Paracetamol1");

        when(diseaseService.add(request)).thenThrow(MethodArgumentNotValidException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/disease")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Error validation"))
                .andExpect(jsonPath("$.error.name").value("name must be alphabet"))
                .andExpect(jsonPath("$.error.medicine").value("medicine must be alphabet"));
    }

    @Test
    public void whenUpdateRequestToDisease_thenCorrectResponse() throws Exception {
        UUID id = UUID.randomUUID();
        DiseaseRequest request = new DiseaseRequest();
        request.setName("Flu");
        request.setMedicine("Paracetamol");

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", request);

        when(diseaseService.update(id.toString(), request)).thenReturn(responseData);

        mockMvc.perform(MockMvcRequestBuilders.put("/disease/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(responseData.getCode()))
                .andExpect(jsonPath("$.message").value(responseData.getMessage()))
                .andExpect(jsonPath("$.data").value(responseData.getData()));

        verify(diseaseService, times(1)).update(id.toString(), request);
    }

    @Test
    public void whenDeleteRequestToDisease_thenCorrectResponse() throws Exception {
        UUID id = UUID.randomUUID();

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", null);

        when(diseaseService.delete(id.toString())).thenReturn(responseData);

        mockMvc.perform(MockMvcRequestBuilders.delete("/disease/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(responseData.getCode()))
                .andExpect(jsonPath("$.message").value(responseData.getMessage()))
                .andExpect(jsonPath("$.data").value(responseData.getData()));

        verify(diseaseService, times(1)).delete(id.toString());
    }

    @Test
    public void whenDeleteRequestToDisease_thenIsAlreadyDeletedResponse() throws Exception {
        UUID id = UUID.randomUUID();

        when(diseaseService.delete(id.toString())).thenThrow(new NotProcessException("Disease is already deleted"));

        mockMvc.perform(MockMvcRequestBuilders.delete("/disease/{id}", id))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.UNPROCESSABLE_ENTITY.value()))
                .andExpect(jsonPath("$.message").value("Disease is already deleted"))
                .andExpect(jsonPath("$.error").isEmpty());

        verify(diseaseService, times(1)).delete(id.toString());
    }

    @Test
    public void whenRecoveryRequestToDisease_thenCorrectResponse() throws Exception {
        UUID id = UUID.randomUUID();

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", null);

        when(diseaseService.recovery(id.toString())).thenReturn(responseData);

        mockMvc.perform(MockMvcRequestBuilders.patch("/disease/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(responseData.getCode()))
                .andExpect(jsonPath("$.message").value(responseData.getMessage()))
                .andExpect(jsonPath("$.data").value(responseData.getData()));

        verify(diseaseService, times(1)).recovery(id.toString());
    }

    @Test
    public void whenRecoveryRequestToDisease_thenIsAlreadyRecoveredResponse() throws Exception {
        UUID id = UUID.randomUUID();

        when(diseaseService.recovery(id.toString())).thenThrow(new NotProcessException("Disease is already recovered"));

        mockMvc.perform(MockMvcRequestBuilders.patch("/disease/{id}", id))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.UNPROCESSABLE_ENTITY.value()))
                .andExpect(jsonPath("$.message").value("Disease is already recovered"))
                .andExpect(jsonPath("$.error").isEmpty());

        verify(diseaseService, times(1)).recovery(id.toString());
    }
}
