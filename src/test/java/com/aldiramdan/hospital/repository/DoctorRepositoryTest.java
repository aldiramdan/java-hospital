package com.aldiramdan.hospital.repository;

import com.aldiramdan.hospital.model.entity.Doctor;
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
public class DoctorRepositoryTest {
    @Autowired
    private DoctorRepository doctorRepository;

    @Test
    public void testFindAllToDoctor_thenCorrect() {
        Doctor doctor1 = new Doctor();
        doctor1.setName("Dr. John Doe");
        doctor1.setSpecialization("Dermatologist");
        doctor1.setConsultationFee(71922);

        Doctor doctor2 = new Doctor();
        doctor2.setName("Dr. Alice Smith");
        doctor2.setSpecialization("Orthopedic Surgeon");
        doctor2.setConsultationFee(74179);

        doctorRepository.saveAll(List.of(doctor1, doctor2));

        List<Doctor> listDoctor = doctorRepository.findAll();

        assertEquals(2, listDoctor.size());
    }

    @Test
    public void testFindByNameContainingToDoctor_thenCorrect() {
        Doctor doctor1 = new Doctor();
        doctor1.setName("Dr. John Doe");
        doctor1.setSpecialization("Dermatologist");
        doctor1.setConsultationFee(71922);

        Doctor doctor2 = new Doctor();
        doctor2.setName("Dr. Alice Smith");
        doctor2.setSpecialization("Orthopedic Surgeon");
        doctor2.setConsultationFee(74179);

        doctorRepository.saveAll(List.of(doctor1, doctor2));

        List<Doctor> listDoctor = doctorRepository.findByNameContaining("Alice");

        assertEquals(1, listDoctor.size());
        assertEquals("Dr. Alice Smith", listDoctor.get(0).getName());
    }

    @Test
    public void testSaveAndFindByIdToDoctor_thenCorrect() {
        Doctor doctor = new Doctor();
        doctor.setName("Dr. John Doe");
        doctor.setSpecialization("Dermatologist");
        doctor.setConsultationFee(71922);

        doctor = doctorRepository.save(doctor);

        Optional<Doctor> foundDoctor = doctorRepository.findById(doctor.getId());

        assertTrue(foundDoctor.isPresent());
        assertEquals("Dr. John Doe", foundDoctor.get().getName());
        assertEquals("Dermatologist", foundDoctor.get().getSpecialization());
        assertEquals(71922, foundDoctor.get().getConsultationFee());
    }

    @Test
    public void testUpdateToDoctor_thenCorrect() {
        Doctor doctor = new Doctor();
        doctor.setName("Dr. John Doe");
        doctor.setSpecialization("Dermatologist");
        doctor.setConsultationFee(71922);

        doctor = doctorRepository.save(doctor);

        doctor.setName("Dr. John Doe Updated");
        doctor.setSpecialization("Dermatologist Updated");
        doctor.setConsultationFee(81923);

        doctorRepository.save(doctor);

        Optional<Doctor> updatedDoctor = doctorRepository.findById(doctor.getId());

        assertTrue(updatedDoctor.isPresent());
        assertEquals("Dr. John Doe Updated", updatedDoctor.get().getName());
        assertEquals("Dermatologist Updated", updatedDoctor.get().getSpecialization());
        assertEquals(81923, updatedDoctor.get().getConsultationFee());
    }

    @Test
    public void testDeleteByIdToDoctor_thenCorrect() {
        Doctor doctor = new Doctor();
        doctor.setName("Dr. John Doe");
        doctor.setSpecialization("Dermatologist");
        doctor.setConsultationFee(71922);

        doctor = doctorRepository.save(doctor);

        doctorRepository.deleteById(doctor.getId());

        Optional<Doctor> deletedDoctor = doctorRepository.findById(doctor.getId());

        assertFalse(deletedDoctor.isPresent());
    }
}
