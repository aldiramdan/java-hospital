package com.aldiramdan.hospital.repository;

import com.aldiramdan.hospital.model.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TreatmentRepositoryTest {
    @Autowired
    private TreatmentRepository treatmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private DiseaseRepository diseaseRepository;

    @Test
    public void testFindAllToTreatment_thenCorrect() {
        Patient patient1 = new Patient();
        patient1.setName("John Doe");
        patient1.setAge(30);
        patient1.setAddress("123 Main St");

        Patient patient2 = new Patient();
        patient2.setName("Alice Smith");
        patient2.setAge(25);
        patient2.setAddress("456 Elm St");

        Doctor doctor1 = new Doctor();
        doctor1.setName("Dr. John Doe");
        doctor1.setSpecialization("Dermatologist");
        doctor1.setConsultationFee(71922);

        Doctor doctor2 = new Doctor();
        doctor2.setName("Dr. Alice Smith");
        doctor2.setSpecialization("Orthopedic Surgeon");
        doctor2.setConsultationFee(74179);

        Medicine medicine1 = new Medicine();
        medicine1.setName("Paracetamol");
        medicine1.setPrice(3199);

        Medicine medicine2 = new Medicine();
        medicine2.setName("Amoxicillin");
        medicine2.setPrice(3339);

        Disease disease1 = new Disease();
        disease1.setName("Flu");
        disease1.setMedicine(medicine1);

        Disease disease2 = new Disease();
        disease2.setName("Fever");
        disease2.setMedicine(medicine2);

        List<Disease> listDisease = new ArrayList<>();
        listDisease.add(disease1);
        listDisease.add(disease2);

        Treatment treatment1 = new Treatment();
        treatment1.setPatient(patient1);
        treatment1.setDoctor(doctor1);
        treatment1.setDisease(listDisease);

        Treatment treatment2 = new Treatment();
        treatment2.setPatient(patient2);
        treatment2.setDoctor(doctor2);
        treatment2.setDisease(listDisease);

        patientRepository.saveAll(List.of(patient1, patient2));

        doctorRepository.saveAll(List.of(doctor1, doctor2));

        medicineRepository.saveAll(List.of(medicine1, medicine2));

        diseaseRepository.saveAll(List.of(disease1, disease2));

        treatmentRepository.saveAll(List.of(treatment1, treatment2));

        List<Treatment> listTreatment = treatmentRepository.findAll();

        assertEquals(2, listTreatment.size());
    }

    @Test
    public void testFindByPatientNameToTreatment_thenCorrect() {
        Patient patient1 = new Patient();
        patient1.setName("John Doe");
        patient1.setAge(30);
        patient1.setAddress("123 Main St");

        Patient patient2 = new Patient();
        patient2.setName("Alice Smith");
        patient2.setAge(25);
        patient2.setAddress("456 Elm St");

        Doctor doctor1 = new Doctor();
        doctor1.setName("Dr. John Doe");
        doctor1.setSpecialization("Dermatologist");
        doctor1.setConsultationFee(71922);

        Doctor doctor2 = new Doctor();
        doctor2.setName("Dr. Alice Smith");
        doctor2.setSpecialization("Orthopedic Surgeon");
        doctor2.setConsultationFee(74179);

        Medicine medicine1 = new Medicine();
        medicine1.setName("Paracetamol");
        medicine1.setPrice(3199);

        Medicine medicine2 = new Medicine();
        medicine2.setName("Amoxicillin");
        medicine2.setPrice(3339);

        Disease disease1 = new Disease();
        disease1.setName("Flu");
        disease1.setMedicine(medicine1);

        Disease disease2 = new Disease();
        disease2.setName("Fever");
        disease2.setMedicine(medicine2);

        List<Disease> listDisease = new ArrayList<>();
        listDisease.add(disease1);
        listDisease.add(disease2);

        Treatment treatment1 = new Treatment();
        treatment1.setPatient(patient1);
        treatment1.setDoctor(doctor1);
        treatment1.setDisease(listDisease);

        Treatment treatment2 = new Treatment();
        treatment2.setPatient(patient2);
        treatment2.setDoctor(doctor2);
        treatment2.setDisease(listDisease);

        patientRepository.saveAll(List.of(patient1, patient2));

        doctorRepository.saveAll(List.of(doctor1, doctor2));

        medicineRepository.saveAll(List.of(medicine1, medicine2));

        diseaseRepository.saveAll(List.of(disease1, disease2));

        treatmentRepository.saveAll(List.of(treatment1, treatment2));

        List<Treatment> listTreatment = treatmentRepository.findByPatientName("John");

        assertEquals(1, listTreatment.size());
        assertEquals("John Doe", listTreatment.get(0).getPatient().getName());
    }

    @Test
    public void testSaveAndFindByIdToTreatment_thenCorrect() {
        Patient patient = new Patient();
        patient.setName("John Doe");
        patient.setAge(30);
        patient.setAddress("123 Main St");

        Doctor doctor = new Doctor();
        doctor.setName("Dr. John Doe");
        doctor.setSpecialization("Dermatologist");
        doctor.setConsultationFee(71922);

        Medicine medicine1 = new Medicine();
        medicine1.setName("Paracetamol");
        medicine1.setPrice(3199);

        Medicine medicine2 = new Medicine();
        medicine2.setName("Amoxicillin");
        medicine2.setPrice(3339);

        Disease disease1 = new Disease();
        disease1.setName("Flu");
        disease1.setMedicine(medicine1);

        Disease disease2 = new Disease();
        disease2.setName("Fever");
        disease2.setMedicine(medicine2);

        List<Disease> listDisease = new ArrayList<>();
        listDisease.add(disease1);
        listDisease.add(disease2);

        Treatment treatment = new Treatment();
        treatment.setPatient(patient);
        treatment.setDoctor(doctor);
        treatment.setDisease(listDisease);

        patientRepository.save(patient);

        doctorRepository.save(doctor);

        medicineRepository.saveAll(List.of(medicine1, medicine2));

        diseaseRepository.saveAll(List.of(disease1, disease2));

        treatment = treatmentRepository.save(treatment);

        Optional<Treatment> foundTreatment = treatmentRepository.findById(treatment.getId());

        assertTrue(foundTreatment.isPresent());
        assertEquals("John Doe", foundTreatment.get().getPatient().getName());
        assertEquals(30, foundTreatment.get().getPatient().getAge());
        assertEquals("123 Main St", foundTreatment.get().getPatient().getAddress());
        assertEquals("Dr. John Doe", foundTreatment.get().getDoctor().getName());
        assertEquals("Dermatologist", foundTreatment.get().getDoctor().getSpecialization());
        assertEquals(71922, foundTreatment.get().getDoctor().getConsultationFee());
        assertEquals("Flu", foundTreatment.get().getDisease().get(0).getName());
        assertEquals("Paracetamol", foundTreatment.get().getDisease().get(0).getMedicine().getName());
        assertEquals("Fever", foundTreatment.get().getDisease().get(1).getName());
        assertEquals("Amoxicillin", foundTreatment.get().getDisease().get(1).getMedicine().getName());
        assertEquals(2, foundTreatment.get().getDisease().size());
    }

    @Test
    public void testUpdateToTreatment_thenCorrect() {
        Patient patient1 = new Patient();
        patient1.setName("John Doe");
        patient1.setAge(30);
        patient1.setAddress("123 Main St");

        Patient patient2 = new Patient();
        patient2.setName("Alice Smith");
        patient2.setAge(25);
        patient2.setAddress("456 Elm St");

        Doctor doctor1 = new Doctor();
        doctor1.setName("Dr. John Doe");
        doctor1.setSpecialization("Dermatologist");
        doctor1.setConsultationFee(71922);

        Doctor doctor2 = new Doctor();
        doctor2.setName("Dr. Alice Smith");
        doctor2.setSpecialization("Orthopedic Surgeon");
        doctor2.setConsultationFee(74179);

        Medicine medicine1 = new Medicine();
        medicine1.setName("Paracetamol");
        medicine1.setPrice(3199);

        Medicine medicine2 = new Medicine();
        medicine2.setName("Amoxicillin");
        medicine2.setPrice(3339);

        Disease disease1 = new Disease();
        disease1.setName("Flu");
        disease1.setMedicine(medicine1);

        Disease disease2 = new Disease();
        disease2.setName("Fever");
        disease2.setMedicine(medicine2);

        List<Disease> listDisease1 = new ArrayList<>();
        listDisease1.add(disease1);
        listDisease1.add(disease2);

        List<Disease> listDisease2 = new ArrayList<>();
        listDisease2.add(disease2);

        Treatment treatment = new Treatment();
        treatment.setPatient(patient1);
        treatment.setDoctor(doctor1);
        treatment.setDisease(listDisease1);

        patientRepository.saveAll(List.of(patient1, patient2));

        doctorRepository.saveAll(List.of(doctor1, doctor2));

        medicineRepository.saveAll(List.of(medicine1, medicine2));

        diseaseRepository.saveAll(List.of(disease1, disease2));

        treatment = treatmentRepository.save(treatment);

        treatment.setPatient(patient2);
        treatment.setDoctor(doctor2);
        treatment.setDisease(listDisease2);

        treatmentRepository.save(treatment);

        Optional<Treatment> updatedTreatment = treatmentRepository.findById(treatment.getId());

        assertTrue(updatedTreatment.isPresent());
        assertEquals("Alice Smith", updatedTreatment.get().getPatient().getName());
        assertEquals(25, updatedTreatment.get().getPatient().getAge());
        assertEquals("456 Elm St", updatedTreatment.get().getPatient().getAddress());
        assertEquals("Dr. Alice Smith", updatedTreatment.get().getDoctor().getName());
        assertEquals("Orthopedic Surgeon", updatedTreatment.get().getDoctor().getSpecialization());
        assertEquals(74179, updatedTreatment.get().getDoctor().getConsultationFee());
        assertEquals("Fever", updatedTreatment.get().getDisease().get(0).getName());
        assertEquals("Amoxicillin", updatedTreatment.get().getDisease().get(0).getMedicine().getName());
        assertEquals(1, updatedTreatment.get().getDisease().size());
    }

    @Test
    public void testDeleteByIdToTreatment_thenCorrect() {
        Patient patient = new Patient();
        patient.setName("John Doe");
        patient.setAge(30);
        patient.setAddress("123 Main St");

        Doctor doctor = new Doctor();
        doctor.setName("Dr. John Doe");
        doctor.setSpecialization("Dermatologist");
        doctor.setConsultationFee(71922);

        Medicine medicine1 = new Medicine();
        medicine1.setName("Paracetamol");
        medicine1.setPrice(3199);

        Medicine medicine2 = new Medicine();
        medicine2.setName("Amoxicillin");
        medicine2.setPrice(3339);

        Disease disease1 = new Disease();
        disease1.setName("Flu");
        disease1.setMedicine(medicine1);

        Disease disease2 = new Disease();
        disease2.setName("Fever");
        disease2.setMedicine(medicine2);

        List<Disease> listDisease = new ArrayList<>();
        listDisease.add(disease1);
        listDisease.add(disease2);

        Treatment treatment = new Treatment();
        treatment.setPatient(patient);
        treatment.setDoctor(doctor);
        treatment.setDisease(listDisease);

        patientRepository.save(patient);

        doctorRepository.save(doctor);

        medicineRepository.saveAll(List.of(medicine1, medicine2));

        diseaseRepository.saveAll(List.of(disease1, disease2));

        treatment = treatmentRepository.save(treatment);

        treatmentRepository.deleteById(treatment.getId());

        Optional<Treatment> deletedTreatment = treatmentRepository.findById(treatment.getId());

        assertFalse(deletedTreatment.isPresent());
    }
}
