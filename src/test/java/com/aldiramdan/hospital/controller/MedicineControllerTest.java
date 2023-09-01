package com.aldiramdan.hospital.controller;

import com.aldiramdan.hospital.exception.custom.NotFoundException;
import com.aldiramdan.hospital.exception.custom.NotProcessException;
import com.aldiramdan.hospital.model.dto.request.MedicineRequest;
import com.aldiramdan.hospital.model.dto.response.ResponseData;
import com.aldiramdan.hospital.model.entity.Medicine;
import com.aldiramdan.hospital.service.MedicineService;
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
public class MedicineControllerTest {
    @MockBean
    private MedicineService medicineService;

    @Autowired
    private MedicineController medicineController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void init() throws Exception {
        Medicine medicine = new Medicine();

        List<Medicine> listMedicine = new ArrayList<>();
        listMedicine.add(new Medicine());
        listMedicine.add(new Medicine());

        MedicineRequest request = new MedicineRequest();
        request.setName("Paracetamol");
        request.setPrice(4355);

        MedicineRequest requestInvalid = new MedicineRequest();
        requestInvalid.setName("Paracetamol1");
        requestInvalid.setPrice(-4355);

        ResponseData responseMedicine = new ResponseData(HttpStatus.OK.value(), "Success", medicine);
        ResponseData responseAddMedicine = new ResponseData(HttpStatus.CREATED.value(), "Success", medicine);
        ResponseData responseListMedicine = new ResponseData(HttpStatus.OK.value(), "Success", listMedicine);
        ResponseData responseMedicineNull = new ResponseData(HttpStatus.OK.value(), "Success", null);

        lenient().when(medicineService.getAll()).thenReturn(responseListMedicine);
        lenient().when(medicineService.getById("1")).thenReturn(responseMedicine);
        lenient().when(medicineService.getById("2")).thenThrow(new NotFoundException("Medicine not found"));
        lenient().when(medicineService.getByName(anyString())).thenReturn(responseListMedicine);
        lenient().when(medicineService.add(request)).thenReturn(responseAddMedicine);
        lenient().when(medicineService.add(requestInvalid)).thenThrow(MethodArgumentNotValidException.class);
        lenient().when(medicineService.update("1", request)).thenReturn(responseMedicine);
        lenient().when(medicineService.delete("1")).thenReturn(responseMedicineNull);
        lenient().when(medicineService.delete("2")).thenThrow(new NotProcessException("Medicine is already deleted"));
        lenient().when(medicineService.recovery("1")).thenReturn(responseMedicineNull);
        lenient().when(medicineService.recovery("2")).thenThrow(new NotProcessException("Medicine is already recovered"));
    }

    @Test
    public void whenMedicineControllerInjected_thenNotNull() throws Exception {
        assertThat(medicineController).isNotNull();
    }

    @Test
    public void whenGetAllRequestToMedicine_thenCorrectResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/medicine"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isArray());

        verify(medicineService, times(1)).getAll();
    }

    @Test
    public void whenGetByIdRequestToMedicine_thenCorrectResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/medicine/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isNotEmpty());

        verify(medicineService, times(1)).getById("1");
    }

    @Test
    public void whenGetByIdRequestToMedicine_thenNotFoundResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/medicine/{id}", "2"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Medicine not found"))
                .andExpect(jsonPath("$.error").isEmpty());

        verify(medicineService, times(1)).getById("2");
    }

    @Test
    public void whenGetByNameRequestToMedicine_thenCorrectResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/medicine/search?name={name}", anyString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isArray());

        verify(medicineService, times(1)).getByName(anyString());
    }

    @Test
    public void whenAddRequestToMedicine_thenCorrectResponse() throws Exception {
        MedicineRequest request = new MedicineRequest();
        request.setName("Paracetamol");
        request.setPrice(4355);

        mockMvc.perform(MockMvcRequestBuilders.post("/medicine")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isNotEmpty());

        verify(medicineService, times(1)).add(request);
    }

    @Test
    public void whenAddRequestToMedicine_thenMethodInvalidResponse() throws Exception {
        MedicineRequest request = new MedicineRequest();
        request.setName("Paracetamol1");
        request.setPrice(-4355);

        mockMvc.perform(MockMvcRequestBuilders.post("/medicine")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Error validation"))
                .andExpect(jsonPath("$.error.name").value("name must be alphabet"))
                .andExpect(jsonPath("$.error.price").value("price must be positive"));
    }

    @Test
    public void whenUpdateRequestToMedicine_thenCorrectResponse() throws Exception {
        MedicineRequest request = new MedicineRequest();
        request.setName("Paracetamol");
        request.setPrice(4355);

        mockMvc.perform(MockMvcRequestBuilders.put("/medicine/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isNotEmpty());

        verify(medicineService, times(1)).update("1", request);
    }

    @Test
    public void whenDeleteRequestToMedicine_thenCorrectResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/medicine/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isEmpty());

        verify(medicineService, times(1)).delete("1");
    }

    @Test
    public void whenDeleteRequestToMedicine_thenIsAlreadyDeletedResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/medicine/{id}", "2"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.UNPROCESSABLE_ENTITY.value()))
                .andExpect(jsonPath("$.message").value("Medicine is already deleted"))
                .andExpect(jsonPath("$.error").isEmpty());

        verify(medicineService, times(1)).delete("2");
    }

    @Test
    public void whenRecoveryRequestToMedicine_thenCorrectResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/medicine/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isEmpty());

        verify(medicineService, times(1)).recovery("1");
    }

    @Test
    public void whenRecoveryRequestToMedicine_thenIsAlreadyRecoveredResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/medicine/{id}", "2"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.UNPROCESSABLE_ENTITY.value()))
                .andExpect(jsonPath("$.message").value("Medicine is already recovered"))
                .andExpect(jsonPath("$.error").isEmpty());

        verify(medicineService, times(1)).recovery("2");
    }
}
