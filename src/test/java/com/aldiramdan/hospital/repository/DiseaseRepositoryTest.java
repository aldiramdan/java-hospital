package com.aldiramdan.hospital.repository;

import com.aldiramdan.hospital.model.entity.Disease;
import com.aldiramdan.hospital.model.entity.Medicine;
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
public class DiseaseRepositoryTest {
    @Autowired
    private DiseaseRepository diseaseRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    @Test
    public void testFindAllToDisease_thenCorrect() {
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

        medicineRepository.saveAll(List.of(medicine1, medicine2));

        diseaseRepository.saveAll(List.of(disease1, disease2));

        List<Disease> listDisease = diseaseRepository.findAll();

        assertEquals(2, listDisease.size());
    }

    @Test
    public void testFindByNameContainingToDisease_thenCorrect() {
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

        medicineRepository.saveAll(List.of(medicine1, medicine2));

        diseaseRepository.saveAll(List.of(disease1, disease2));

        List<Disease> listDisease = diseaseRepository.findByNameContaining("Flu");

        assertEquals(1, listDisease.size());
        assertEquals("Flu", listDisease.get(0).getName());
        assertEquals("Paracetamol", listDisease.get(0).getMedicine().getName());
    }

    @Test
    public void testSaveAndFindByIdToDisease_thenCorrect() {
        Medicine medicine = new Medicine();
        medicine.setName("Paracetamol");
        medicine.setPrice(3199);

        Disease disease = new Disease();
        disease.setName("Flu");
        disease.setMedicine(medicine);

        medicine = medicineRepository.save(medicine);

        disease = diseaseRepository.save(disease);

        Optional<Disease> foundDisease = diseaseRepository.findById(disease.getId());

        assertTrue(foundDisease.isPresent());
        assertEquals("Flu", foundDisease.get().getName());
        assertEquals("Paracetamol", foundDisease.get().getMedicine().getName());
    }

    @Test
    public void testUpdateToDisease_thenCorrect() {
        Medicine medicine1 = new Medicine();
        medicine1.setName("Paracetamol");
        medicine1.setPrice(3199);

        Medicine medicine2 = new Medicine();
        medicine2.setName("Amoxicillin");
        medicine2.setPrice(3339);

        Disease disease = new Disease();
        disease.setName("Flu");
        disease.setMedicine(medicine1);

        medicineRepository.saveAll(List.of(medicine1, medicine2));

        disease = diseaseRepository.save(disease);

        disease.setName("Flu Updated");
        disease.setMedicine(medicine2);

        diseaseRepository.save(disease);

        Optional<Disease> updatedDisease = diseaseRepository.findById(disease.getId());

        assertTrue(updatedDisease.isPresent());
        assertEquals("Flu Updated", updatedDisease.get().getName());
        assertEquals("Amoxicillin", updatedDisease.get().getMedicine().getName());
    }

    @Test
    public void testDeleteByIdToDisease_thenCorrect() {
        Medicine medicine = new Medicine();
        medicine.setName("Paracetamol");
        medicine.setPrice(3199);

        Disease disease = new Disease();
        disease.setName("Flu");
        disease.setMedicine(medicine);

        medicine = medicineRepository.save(medicine);

        disease = diseaseRepository.save(disease);

        diseaseRepository.deleteById(disease.getId());

        Optional<Disease> deletedDisease = diseaseRepository.findById(disease.getId());

        assertFalse(deletedDisease.isPresent());
    }
}
