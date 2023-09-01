package com.aldiramdan.hospital.controller;

import com.aldiramdan.hospital.exception.custom.NotFoundException;
import com.aldiramdan.hospital.exception.custom.NotProcessException;
import com.aldiramdan.hospital.model.dto.request.DoctorRequest;
import com.aldiramdan.hospital.model.dto.response.ResponseData;
import com.aldiramdan.hospital.model.entity.Doctor;
import com.aldiramdan.hospital.service.DoctorService;
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
public class DoctorControllerTest {
    @MockBean
    private DoctorService doctorService;

    @Autowired
    private DoctorController doctorController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void init() throws Exception {
        Doctor doctor = new Doctor();

        List<Doctor> listDoctor = new ArrayList<>();
        listDoctor.add(new Doctor());
        listDoctor.add(new Doctor());

        DoctorRequest request = new DoctorRequest();
        request.setName("Dr. John Smith");
        request.setSpecialization("Dermatologist");
        request.setConsultationFee(71922);

        DoctorRequest requestInvalid = new DoctorRequest();
        requestInvalid.setName("Dr. John Smith1");
        requestInvalid.setSpecialization("Dermatologist1");
        requestInvalid.setConsultationFee(-71922);

        ResponseData responseDoctor = new ResponseData(HttpStatus.OK.value(), "Success", doctor);
        ResponseData responseAddDoctor = new ResponseData(HttpStatus.CREATED.value(), "Success", doctor);
        ResponseData responseListDoctor = new ResponseData(HttpStatus.OK.value(), "Success", listDoctor);
        ResponseData responseDoctorNull = new ResponseData(HttpStatus.OK.value(), "Success", null);

        lenient().when(doctorService.getAll()).thenReturn(responseListDoctor);
        lenient().when(doctorService.getById("1")).thenReturn(responseDoctor);
        lenient().when(doctorService.getById("2")).thenThrow(new NotFoundException("Doctor not found"));
        lenient().when(doctorService.getByName(anyString())).thenReturn(responseListDoctor);
        lenient().when(doctorService.add(request)).thenReturn(responseAddDoctor);
        lenient().when(doctorService.add(requestInvalid)).thenThrow(MethodArgumentNotValidException.class);
        lenient().when(doctorService.update("1", request)).thenReturn(responseDoctor);
        lenient().when(doctorService.delete("1")).thenReturn(responseDoctorNull);
        lenient().when(doctorService.delete("2")).thenThrow(new NotProcessException("Doctor is already deleted"));
        lenient().when(doctorService.recovery("1")).thenReturn(responseDoctorNull);
        lenient().when(doctorService.recovery("2")).thenThrow(new NotProcessException("Doctor is already recovered"));
    }

    @Test
    public void whenDoctorControllerInjected_thenNotNull() throws Exception {
        assertThat(doctorController).isNotNull();
    }

    @Test
    public void whenGetAllRequestToDoctor_thenCorrectResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/doctor"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isArray());

        verify(doctorService, times(1)).getAll();
    }

    @Test
    public void whenGetByIdRequestToDoctor_thenCorrectResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/doctor/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isNotEmpty());

        verify(doctorService, times(1)).getById("1");
    }

    @Test
    public void whenGetByIdRequestToDoctor_thenNotFoundResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/doctor/{id}", "2"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Doctor not found"))
                .andExpect(jsonPath("$.error").isEmpty());

        verify(doctorService, times(1)).getById("2");
    }

    @Test
    public void whenGetByNameRequestToDoctor_thenCorrectResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/doctor/search?name={name}", anyString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isArray());

        verify(doctorService, times(1)).getByName(anyString());
    }

    @Test
    public void whenAddRequestToDoctor_thenCorrectResponse() throws Exception {
        DoctorRequest request = new DoctorRequest();
        request.setName("Dr. John Smith");
        request.setSpecialization("Dermatologist");
        request.setConsultationFee(71922);

        mockMvc.perform(MockMvcRequestBuilders.post("/doctor", request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isNotEmpty());

        verify(doctorService, times(1)).add(request);
    }

    @Test
    public void whenAddRequestToDoctor_thenMethodInvalidResponse() throws Exception {
        DoctorRequest request = new DoctorRequest();
        request.setName("Dr. John Smith1");
        request.setSpecialization("Dermatologist1");
        request.setConsultationFee(-71922);

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
        DoctorRequest request = new DoctorRequest();
        request.setName("Dr. John Smith");
        request.setSpecialization("Dermatologist");
        request.setConsultationFee(71922);

        mockMvc.perform(MockMvcRequestBuilders.put("/doctor/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isNotEmpty());

        verify(doctorService, times(1)).update("1", request);
    }

    @Test
    public void whenDeleteRequestToDoctor_thenCorrectResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/doctor/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isEmpty());

        verify(doctorService, times(1)).delete("1");
    }

    @Test
    public void whenDeleteRequestToDoctor_thenIsAlreadyDeletedResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/doctor/{id}", "2"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.UNPROCESSABLE_ENTITY.value()))
                .andExpect(jsonPath("$.message").value("Doctor is already deleted"))
                .andExpect(jsonPath("$.error").isEmpty());

        verify(doctorService, times(1)).delete("2");
    }

    @Test
    public void whenRecoveryRequestToDoctor_thenCorrectResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/doctor/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isEmpty());

        verify(doctorService, times(1)).recovery("1");
    }

    @Test
    public void whenRecoveryRequestToDoctor_thenIsAlreadyRecoveredResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/doctor/{id}", "2"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.UNPROCESSABLE_ENTITY.value()))
                .andExpect(jsonPath("$.message").value("Doctor is already recovered"))
                .andExpect(jsonPath("$.error").isEmpty());

        verify(doctorService, times(1)).recovery("2");
    }
}
