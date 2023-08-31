package com.aldiramdan.hospital.repository;

import com.aldiramdan.hospital.model.entity.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PatientRepositoryTest {
    @Autowired
    private PatientRepository patientRepository;

    @Test
    public void testFindAllToPatient_thenCorrect() {
        Patient patient1 = new Patient();
        patient1.setName("John Doe");
        patient1.setAge(30);
        patient1.setAddress("123 Main St");

        Patient patient2 = new Patient();
        patient2.setName("Alice Smith");
        patient2.setAge(25);
        patient2.setAddress("456 Elm St");

        patientRepository.saveAll(List.of(patient1, patient2));

        List<Patient> listPatient = patientRepository.findAll();

        assertEquals(2, listPatient.size());
    }

    @Test
    public void testFindByNameContainingToPatient_thenCorrect() {
        Patient patient1 = new Patient();
        patient1.setName("John Doe");
        patient1.setAge(30);
        patient1.setAddress("123 Main St");

        Patient patient2 = new Patient();
        patient2.setName("Alice Smith");
        patient2.setAge(25);
        patient2.setAddress("456 Elm St");

        patientRepository.saveAll(List.of(patient1, patient2));

        List<Patient> listPatient = patientRepository.findByNameContaining("Alice");

        assertEquals(1, listPatient.size());
        assertEquals("Alice Smith", listPatient.get(0).getName());
    }

    @Test
    public void testSaveAndFindByIdToPatient_thenCorrect() {
        Patient patient = new Patient();
        patient.setName("John Doe");
        patient.setAge(30);
        patient.setAddress("123 Main St");

        patient = patientRepository.save(patient);

        Optional<Patient> foundPatient = patientRepository.findById(patient.getId());

        assertTrue(foundPatient.isPresent());
        assertEquals("John Doe", foundPatient.get().getName());
        assertEquals(30, foundPatient.get().getAge());
        assertEquals("123 Main St", foundPatient.get().getAddress());
    }

    @Test
    public void testUpdateToPatient_thenCorrect() {
        Patient patient = new Patient();
        patient.setName("John Doe");
        patient.setAge(30);
        patient.setAddress("123 Main St");

        patient = patientRepository.save(patient);

        patient.setName("John Doe Updated");
        patient.setAge(35);
        patient.setAddress("123 Main St Updated");

        patientRepository.save(patient);

        Optional<Patient> updatedPatient = patientRepository.findById(patient.getId());

        assertTrue(updatedPatient.isPresent());
        assertEquals("John Doe Updated", updatedPatient.get().getName());
        assertEquals(35, updatedPatient.get().getAge());
        assertEquals("123 Main St Updated", updatedPatient.get().getAddress());
    }

    @Test
    public void testDeleteByIdToPatient_thenCorrect() {
        Patient patient = new Patient();
        patient.setName("John Doe");
        patient.setAge(30);
        patient.setAddress("123 Main St");

        patient = patientRepository.save(patient);

        patientRepository.deleteById(patient.getId());

        Optional<Patient> deletedPatient = patientRepository.findById(patient.getId());

        assertFalse(deletedPatient.isPresent());
    }
}
