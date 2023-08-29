package com.aldiramdan.hospital.controller;

import com.aldiramdan.hospital.exception.custom.NotFoundException;
import com.aldiramdan.hospital.exception.custom.NotProcessException;
import com.aldiramdan.hospital.model.dto.request.MedicineRequest;
import com.aldiramdan.hospital.model.dto.response.ResponseData;
import com.aldiramdan.hospital.model.entity.Medicine;
import com.aldiramdan.hospital.service.MedicineService;
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
@WebMvcTest(MedicineController.class)
@AutoConfigureMockMvc
public class MedicineControllerTest {
    @MockBean
    private MedicineService medicineService;

    @Autowired
    private MedicineController medicineController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenMedicineControllerInjected_thenNotNull() throws Exception {
        assertThat(medicineController).isNotNull();
    }

    @Test
    public void whenGetAllRequestToMedicine_thenCorrectResponse() throws Exception {
        List<Medicine> medicines = new ArrayList<>();
        medicines.add(new Medicine());
        medicines.add(new Medicine());

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", medicines);

        when(medicineService.getAll()).thenReturn(responseData);

        mockMvc.perform(MockMvcRequestBuilders.get("/medicine"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(responseData.getCode()))
                .andExpect(jsonPath("$.message").value(responseData.getMessage()))
                .andExpect(jsonPath("$.data").isArray());

        verify(medicineService, times(1)).getAll();
    }

    @Test
    public void whenGetByIdRequestToMedicine_thenCorrectResponse() throws Exception {
        UUID id = UUID.randomUUID();
        Medicine medicine = new Medicine();
        medicine.setId(id.toString());

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", medicine);

        when(medicineService.getById(id.toString())).thenReturn(responseData);

        mockMvc.perform(MockMvcRequestBuilders.get("/medicine/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(responseData.getCode()))
                .andExpect(jsonPath("$.message").value(responseData.getMessage()))
                .andExpect(jsonPath("$.data").value(responseData.getData()));

        verify(medicineService, times(1)).getById(id.toString());
    }

    @Test
    public void whenGetByIdRequestToMedicine_thenNotFoundResponse() throws Exception {
        UUID id = UUID.randomUUID();
        Medicine patient = new Medicine();
        patient.setId(id.toString());

        when(medicineService.getById(id.toString())).thenThrow(new NotFoundException("Medicine not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/medicine/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Medicine not found"))
                .andExpect(jsonPath("$.error").isEmpty());

        verify(medicineService, times(1)).getById(id.toString());
    }

    @Test
    public void whenGetByNameRequestToMedicine_thenCorrectResponse() throws Exception {
        List<Medicine> medicines = new ArrayList<>();
        medicines.add(new Medicine());
        medicines.add(new Medicine());

        String name = "Flu";

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", medicines);

        when(medicineService.getByName(name)).thenReturn(responseData);

        mockMvc.perform(MockMvcRequestBuilders.get("/medicine/search?name={name}", name))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(responseData.getCode()))
                .andExpect(jsonPath("$.message").value(responseData.getMessage()))
                .andExpect(jsonPath("$.data").isArray());

        verify(medicineService, times(1)).getByName(name);
    }

    @Test
    public void whenAddRequestToMedicine_thenCorrectResponse() throws Exception {
        MedicineRequest request = new MedicineRequest();
        request.setName("Paracetamol");
        request.setPrice(4355);

        ResponseData responseData = new ResponseData(HttpStatus.CREATED.value(), "Success", request);

        when(medicineService.add(request)).thenReturn(responseData);

        mockMvc.perform(MockMvcRequestBuilders.post("/medicine")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(responseData.getCode()))
                .andExpect(jsonPath("$.message").value(responseData.getMessage()))
                .andExpect(jsonPath("$.data").value(responseData.getData()));


        verify(medicineService, times(1)).add(request);
    }

    @Test
    public void whenUpdateRequestToMedicine_thenCorrectResponse() throws Exception {
        UUID id = UUID.randomUUID();
        MedicineRequest request = new MedicineRequest();
        request.setName("Paracetamol");
        request.setPrice(4355);

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", request);

        when(medicineService.update(id.toString(), request)).thenReturn(responseData);

        mockMvc.perform(MockMvcRequestBuilders.put("/medicine/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(responseData.getCode()))
                .andExpect(jsonPath("$.message").value(responseData.getMessage()))
                .andExpect(jsonPath("$.data").value(responseData.getData()));


        verify(medicineService, times(1)).update(id.toString(), request);
    }

    @Test
    public void whenDeleteRequestToMedicine_thenCorrectResponse() throws Exception {
        UUID id = UUID.randomUUID();

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", null);

        when(medicineService.delete(id.toString())).thenReturn(responseData);

        mockMvc.perform(MockMvcRequestBuilders.delete("/medicine/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(responseData.getCode()))
                .andExpect(jsonPath("$.message").value(responseData.getMessage()))
                .andExpect(jsonPath("$.data").value(responseData.getData()));

        verify(medicineService, times(1)).delete(id.toString());
    }

    @Test
    public void whenDeleteRequestToMedicine_thenIsAlreadyDeletedResponse() throws Exception {
        UUID id = UUID.randomUUID();

        when(medicineService.delete(id.toString())).thenThrow(new NotProcessException("Medicine is already deleted"));

        mockMvc.perform(MockMvcRequestBuilders.delete("/medicine/{id}", id))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.UNPROCESSABLE_ENTITY.value()))
                .andExpect(jsonPath("$.message").value("Medicine is already deleted"))
                .andExpect(jsonPath("$.error").isEmpty());

        verify(medicineService, times(1)).delete(id.toString());
    }

    @Test
    public void whenRecoveryRequestToMedicine_thenCorrectResponse() throws Exception {
        UUID id = UUID.randomUUID();

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", null);

        when(medicineService.recovery(id.toString())).thenReturn(responseData);

        mockMvc.perform(MockMvcRequestBuilders.patch("/medicine/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(responseData.getCode()))
                .andExpect(jsonPath("$.message").value(responseData.getMessage()))
                .andExpect(jsonPath("$.data").value(responseData.getData()));

        verify(medicineService, times(1)).recovery(id.toString());
    }

    @Test
    public void whenRecoveryRequestToMedicine_thenIsAlreadyRecoveredResponse() throws Exception {
        UUID id = UUID.randomUUID();

        when(medicineService.recovery(id.toString())).thenThrow(new NotProcessException("Medicine is already recovered"));

        mockMvc.perform(MockMvcRequestBuilders.patch("/medicine/{id}", id))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.UNPROCESSABLE_ENTITY.value()))
                .andExpect(jsonPath("$.message").value("Medicine is already recovered"))
                .andExpect(jsonPath("$.error").isEmpty());

        verify(medicineService, times(1)).recovery(id.toString());
    }
}
