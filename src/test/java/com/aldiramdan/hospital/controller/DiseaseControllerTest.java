package com.aldiramdan.hospital.controller;

import com.aldiramdan.hospital.exception.custom.NotFoundException;
import com.aldiramdan.hospital.exception.custom.NotProcessException;
import com.aldiramdan.hospital.model.dto.request.DiseaseRequest;
import com.aldiramdan.hospital.model.dto.response.ResponseData;
import com.aldiramdan.hospital.model.entity.Disease;
import com.aldiramdan.hospital.service.DiseaseService;
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
public class DiseaseControllerTest {
    @MockBean
    private DiseaseService diseaseService;

    @Autowired
    private DiseaseController diseaseController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void init() throws Exception {
        Disease disease = new Disease();

        List<Disease> listDisease = new ArrayList<>();
        listDisease.add(new Disease());
        listDisease.add(new Disease());

        DiseaseRequest request = new DiseaseRequest();
        request.setName("Flu");
        request.setMedicine("Paracetamol");

        DiseaseRequest requestInvalid = new DiseaseRequest();
        requestInvalid.setName("Flu1");
        requestInvalid.setMedicine("Paracetamol1");

        ResponseData responseDisease = new ResponseData(HttpStatus.OK.value(), "Success", disease);
        ResponseData responseAddDisease = new ResponseData(HttpStatus.CREATED.value(), "Success", disease);
        ResponseData responseListDisease = new ResponseData(HttpStatus.OK.value(), "Success", listDisease);
        ResponseData responseDiseaseNull = new ResponseData(HttpStatus.OK.value(), "Success", null);

        lenient().when(diseaseService.getAll()).thenReturn(responseListDisease);
        lenient().when(diseaseService.getById("1")).thenReturn(responseDisease);
        lenient().when(diseaseService.getById("2")).thenThrow(new NotFoundException("Disease not found"));
        lenient().when(diseaseService.getByName(anyString())).thenReturn(responseListDisease);
        lenient().when(diseaseService.add(request)).thenReturn(responseAddDisease);
        lenient().when(diseaseService.add(requestInvalid)).thenThrow(MethodArgumentNotValidException.class);
        lenient().when(diseaseService.update("1", request)).thenReturn(responseDisease);
        lenient().when(diseaseService.delete("1")).thenReturn(responseDiseaseNull);
        lenient().when(diseaseService.delete("2")).thenThrow(new NotProcessException("Disease is already deleted"));
        lenient().when(diseaseService.recovery("1")).thenReturn(responseDiseaseNull);
        lenient().when(diseaseService.recovery("2")).thenThrow(new NotProcessException("Disease is already recovered"));
    }

    @Test
    public void whenDiseaseControllerInjected_thenNotNull() throws Exception {
        assertThat(diseaseController).isNotNull();
    }

    @Test
    public void whenGetAllRequestToDisease_thenCorrectResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/disease"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isArray());

        verify(diseaseService, times(1)).getAll();
    }

    @Test
    public void whenGetByIdRequestToDisease_thenCorrectResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/disease/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isNotEmpty());

        verify(diseaseService, times(1)).getById("1");
    }

    @Test
    public void whenGetByIdRequestToDisease_thenNotFoundResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/disease/{id}", "2"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Disease not found"))
                .andExpect(jsonPath("$.error").isEmpty());

        verify(diseaseService, times(1)).getById("2");
    }

    @Test
    public void whenGetByNameRequestToDisease_thenCorrectResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/disease/search?name={name}", anyString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isArray());

        verify(diseaseService, times(1)).getByName(anyString());
    }

    @Test
    public void whenAddRequestToDisease_thenCorrectResponse() throws Exception {
        DiseaseRequest request = new DiseaseRequest();
        request.setName("Flu");
        request.setMedicine("Paracetamol");

        mockMvc.perform(MockMvcRequestBuilders.post("/disease")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isNotEmpty());

        verify(diseaseService, times(1)).add(request);
    }

    @Test
    public void whenAddRequestToDisease_thenFailureResponse() throws Exception {
        DiseaseRequest request = new DiseaseRequest();
        request.setName("Flu1");
        request.setMedicine("Paracetamol1");

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
        DiseaseRequest request = new DiseaseRequest();
        request.setName("Flu");
        request.setMedicine("Paracetamol");

        mockMvc.perform(MockMvcRequestBuilders.put("/disease/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isNotEmpty());

        verify(diseaseService, times(1)).update("1", request);
    }

    @Test
    public void whenDeleteRequestToDisease_thenCorrectResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/disease/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isEmpty());

        verify(diseaseService, times(1)).delete("1");
    }

    @Test
    public void whenDeleteRequestToDisease_thenIsAlreadyDeletedResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/disease/{id}", "2"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.UNPROCESSABLE_ENTITY.value()))
                .andExpect(jsonPath("$.message").value("Disease is already deleted"))
                .andExpect(jsonPath("$.error").isEmpty());

        verify(diseaseService, times(1)).delete("2");
    }

    @Test
    public void whenRecoveryRequestToDisease_thenCorrectResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/disease/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isEmpty());

        verify(diseaseService, times(1)).recovery("1");
    }

    @Test
    public void whenRecoveryRequestToDisease_thenIsAlreadyRecoveredResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/disease/{id}", "2"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.UNPROCESSABLE_ENTITY.value()))
                .andExpect(jsonPath("$.message").value("Disease is already recovered"))
                .andExpect(jsonPath("$.error").isEmpty());

        verify(diseaseService, times(1)).recovery("2");
    }
}
