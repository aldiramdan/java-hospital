package com.aldiramdan.hospital.service;

import com.aldiramdan.hospital.model.dto.request.DiseaseRequest;
import com.aldiramdan.hospital.model.dto.request.TreatmentRequest;
import com.aldiramdan.hospital.model.dto.response.*;
import com.aldiramdan.hospital.model.dto.response.ResponseTreatment;
import com.aldiramdan.hospital.model.entity.*;
import com.aldiramdan.hospital.repository.*;
import com.aldiramdan.hospital.repository.TreatmentRepository;
import com.aldiramdan.hospital.service.impl.TreatmentServiceImpl;
import com.aldiramdan.hospital.validator.*;
import com.aldiramdan.hospital.validator.TreatmentValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TreatmentServiceTest {
    @Mock
    private TreatmentRepository treatmentRepository;

    @Mock
    private TreatmentValidator treatmentValidator;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientValidator patientValidator;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private DoctorValidator doctorValidator;

    @Mock
    private DiseaseRepository diseaseRepository;

    @Mock
    private DiseaseValidator diseaseValidator;

    @InjectMocks
    private TreatmentService treatmentService = new TreatmentServiceImpl();

    @BeforeEach
    public void init() {
        Patient patient = new Patient();
        patient.setId("1");
        patient.setName("John");
        patient.setAge(30);
        patient.setAddress("123 Main St");

        Doctor doctor = new Doctor();
        doctor.setId("1");
        doctor.setName("John");
        doctor.setSpecialization("Dermatologist");
        doctor.setConsultationFee(71922);

        Medicine medicine = new Medicine();
        medicine.setId("1");
        medicine.setName("Amoxicillin");
        medicine.setPrice(3339);

        Disease disease = new Disease();
        disease.setId("1");
        disease.setName("Allergies");
        disease.setMedicine(medicine);

        List<Disease> listDisease = new ArrayList<>();
        listDisease.add(disease);
        listDisease.add(disease);
        listDisease.add(disease);

        Treatment treatment = new Treatment();
        treatment.setId("1");
        treatment.setPatient(patient);
        treatment.setDoctor(doctor);
        treatment.setDisease(listDisease);

        List<Treatment> listTreatment = new ArrayList<>();
        listTreatment.add(treatment);
        listTreatment.add(treatment);
        listTreatment.add(treatment);
        listTreatment.add(treatment);
        listTreatment.add(treatment);

        lenient().when(treatmentRepository.findAll()).thenReturn(listTreatment);
        lenient().when(treatmentRepository.findById("1")).thenReturn(Optional.of(treatment));
        lenient().when(treatmentRepository.findByPatientName(anyString())).thenReturn(listTreatment);
        lenient().when(treatmentRepository.save(any())).thenReturn(treatment);

        lenient().when(patientRepository.findById(anyString())).thenReturn(Optional.of(patient));
        lenient().when(doctorRepository.findById(anyString())).thenReturn(Optional.of(doctor));
        lenient().when(diseaseRepository.findByName(anyString())).thenReturn(Optional.of(disease));
    }

    @Test
    public void testGetAllToTreatment_thenCorrect() {
        ResponseData responseData = treatmentService.getAll();

        assertEquals(200, responseData.getCode());
        assertEquals("Success", responseData.getMessage());
        assertEquals(5, ((List) responseData.getData()).size());

        verify(treatmentRepository, times(1)).findAll();
    }

    @Test
    public void testGetByIdToTreatment_thenCorrect() throws Exception {
        ResponseData responseData = treatmentService.getById("1");

        assertNotNull(responseData);
        assertEquals(200, responseData.getCode());
        assertEquals("Success", responseData.getMessage());
        assertEquals("1", ((ResponseTreatment) responseData.getData()).getId());
        assertEquals("1", ((ResponseTreatment) responseData.getData()).getPatient().getId());
        assertEquals("John", ((ResponseTreatment) responseData.getData()).getPatient().getName());
        assertEquals(30, ((ResponseTreatment) responseData.getData()).getPatient().getAge());
        assertEquals("123 Main St", ((ResponseTreatment) responseData.getData()).getPatient().getAddress());
        assertEquals("1", ((ResponseTreatment) responseData.getData()).getDoctor().getId());
        assertEquals("John", ((ResponseTreatment) responseData.getData()).getDoctor().getName());
        assertEquals("Dermatologist", ((ResponseTreatment) responseData.getData()).getDoctor().getSpecialization());
        assertEquals(71922, ((ResponseTreatment) responseData.getData()).getDoctor().getConsultationFee());
        assertEquals(3, ((ResponseTreatment) responseData.getData()).getDisease().size());

        verify(treatmentRepository, times(1)).findById(anyString());
    }

    @Test
    public void testGetByNameToTreatment_thenCorrect() {
        ResponseData responseData = treatmentService.getByPatientName("John");

        assertNotNull(responseData);
        assertEquals(200, responseData.getCode());
        assertEquals("Success", responseData.getMessage());
        assertEquals(5, ((List) responseData.getData()).size());

        verify(treatmentRepository, times(1)).findByPatientName(anyString());
    }

    @Test
    public void testAddToTreatment_thenCorrect() throws Exception {
        DiseaseRequest diseaseRequest = new DiseaseRequest();
        diseaseRequest.setName("Allergies");
        diseaseRequest.setMedicine("Amoxicillin");

        List<DiseaseRequest> listDisease = new ArrayList<>();
        listDisease.add(diseaseRequest);
        listDisease.add(diseaseRequest);
        listDisease.add(diseaseRequest);

        TreatmentRequest request = new TreatmentRequest();
        request.setPatientId("1");
        request.setDoctorId("1");
        request.setDisease(listDisease);

        ResponseData responseData = treatmentService.add(request);

        assertNotNull(responseData);
        assertEquals(201, responseData.getCode());
        assertEquals("Success", responseData.getMessage());
        assertEquals("1", ((ResponseTreatment) responseData.getData()).getId());
        assertEquals("1", ((ResponseTreatment) responseData.getData()).getPatient().getId());
        assertEquals("John", ((ResponseTreatment) responseData.getData()).getPatient().getName());
        assertEquals(30, ((ResponseTreatment) responseData.getData()).getPatient().getAge());
        assertEquals("123 Main St", ((ResponseTreatment) responseData.getData()).getPatient().getAddress());
        assertEquals("1", ((ResponseTreatment) responseData.getData()).getDoctor().getId());
        assertEquals("John", ((ResponseTreatment) responseData.getData()).getDoctor().getName());
        assertEquals("Dermatologist", ((ResponseTreatment) responseData.getData()).getDoctor().getSpecialization());
        assertEquals(71922, ((ResponseTreatment) responseData.getData()).getDoctor().getConsultationFee());
        assertEquals(3, ((ResponseTreatment) responseData.getData()).getDisease().size());

        verify(patientRepository, times(1)).findById(anyString());
        verify(doctorRepository, times(1)).findById(anyString());
        verify(diseaseRepository, times(3)).findByName(anyString());
        verify(treatmentRepository, times(1)).save(any());
    }

    @Test
    public void testUpdateToTreatment_thenCorrect() throws Exception {
        DiseaseRequest diseaseRequest = new DiseaseRequest();
        diseaseRequest.setName("Allergies");
        diseaseRequest.setMedicine("Amoxicillin");

        List<DiseaseRequest> listDisease = new ArrayList<>();
        listDisease.add(diseaseRequest);
        listDisease.add(diseaseRequest);

        TreatmentRequest request = new TreatmentRequest();
        request.setPatientId("1");
        request.setDoctorId("1");
        request.setDisease(listDisease);

        ResponseData responseData = treatmentService.update("1", request);

        assertNotNull(responseData);
        assertEquals(200, responseData.getCode());
        assertEquals("Success", responseData.getMessage());
        assertEquals("1", ((ResponseTreatment) responseData.getData()).getId());
        assertEquals("1", ((ResponseTreatment) responseData.getData()).getPatient().getId());
        assertEquals("John", ((ResponseTreatment) responseData.getData()).getPatient().getName());
        assertEquals(30, ((ResponseTreatment) responseData.getData()).getPatient().getAge());
        assertEquals("123 Main St", ((ResponseTreatment) responseData.getData()).getPatient().getAddress());
        assertEquals("1", ((ResponseTreatment) responseData.getData()).getDoctor().getId());
        assertEquals("John", ((ResponseTreatment) responseData.getData()).getDoctor().getName());
        assertEquals("Dermatologist", ((ResponseTreatment) responseData.getData()).getDoctor().getSpecialization());
        assertEquals(71922, ((ResponseTreatment) responseData.getData()).getDoctor().getConsultationFee());
        assertEquals(2, ((ResponseTreatment) responseData.getData()).getDisease().size());


        verify(treatmentRepository, times(1)).findById(anyString());
        verify(patientRepository, times(1)).findById(anyString());
        verify(doctorRepository, times(1)).findById(anyString());
        verify(diseaseRepository, times(2)).findByName(anyString());
        verify(treatmentRepository, times(1)).save(any());
    }

    @Test
    public void testDeleteToTreatment_thenCorrect() throws Exception {
        ResponseData responseData = treatmentService.delete("1");

        assertNotNull(responseData);
        assertEquals(200, responseData.getCode());
        assertEquals("Successfully deleted treatment", responseData.getMessage());

        verify(treatmentRepository, times(1)).findById(anyString());
        verify(treatmentRepository, times(1)).save(any());
    }

    @Test
    public void testRecoveryToTreatment_thenCorrect() throws Exception {
        ResponseData responseData = treatmentService.recovery("1");

        assertNotNull(responseData);
        assertEquals(200, responseData.getCode());
        assertEquals("Successfully recovered treatment", responseData.getMessage());

        verify(treatmentRepository, times(1)).findById(anyString());
        verify(treatmentRepository, times(1)).save(any());
    }
}
