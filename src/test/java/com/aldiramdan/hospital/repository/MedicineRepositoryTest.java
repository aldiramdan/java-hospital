package com.aldiramdan.hospital.repository;

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
public class MedicineRepositoryTest {
    @Autowired
    private MedicineRepository medicineRepository;

    @Test
    public void testFindAllToMedicine_thenCorrect() {
        Medicine medicine1 = new Medicine();
        medicine1.setName("Paracetamol");
        medicine1.setPrice(3199);

        Medicine medicine2 = new Medicine();
        medicine2.setName("Amoxicillin");
        medicine2.setPrice(3339);

        medicineRepository.saveAll(List.of(medicine1, medicine2));

        List<Medicine> listMedicine = medicineRepository.findAll();

        assertEquals(2, listMedicine.size());
    }

    @Test
    public void testFindByNameContainingToMedicine_thenCorrect() {
        Medicine medicine1 = new Medicine();
        medicine1.setName("Paracetamol");
        medicine1.setPrice(3199);

        Medicine medicine2 = new Medicine();
        medicine2.setName("Amoxicillin");
        medicine2.setPrice(3339);

        medicineRepository.saveAll(List.of(medicine1, medicine2));

        List<Medicine> listMedicine = medicineRepository.findByNameContaining("Parace");

        assertEquals(1, listMedicine.size());
        assertEquals("Paracetamol", listMedicine.get(0).getName());
    }

    @Test
    public void testSaveAndFindByIdToMedicine_thenCorrect() {
        Medicine medicine = new Medicine();
        medicine.setName("Paracetamol");
        medicine.setPrice(3199);

        medicine = medicineRepository.save(medicine);

        Optional<Medicine> foundMedicine = medicineRepository.findById(medicine.getId());

        assertTrue(foundMedicine.isPresent());
        assertEquals("Paracetamol", foundMedicine.get().getName());
        assertEquals(3199, foundMedicine.get().getPrice());
    }

    @Test
    public void testUpdateToMedicine_thenCorrect() {
        Medicine medicine = new Medicine();
        medicine.setName("Paracetamol");
        medicine.setPrice(3199);

        medicine = medicineRepository.save(medicine);

        medicine.setName("Paracetamol Updated");
        medicine.setPrice(4199);

        medicineRepository.save(medicine);

        Optional<Medicine> updatedMedicine = medicineRepository.findById(medicine.getId());

        assertTrue(updatedMedicine.isPresent());
        assertEquals("Paracetamol Updated", updatedMedicine.get().getName());
        assertEquals(4199, updatedMedicine.get().getPrice());
    }

    @Test
    public void testDeleteByIdToMedicine_thenCorrect() {
        Medicine medicine = new Medicine();
        medicine.setName("Paracetamol");
        medicine.setPrice(3199);

        medicine = medicineRepository.save(medicine);

        medicineRepository.deleteById(medicine.getId());

        Optional<Medicine> deletedMedicine = medicineRepository.findById(medicine.getId());

        assertFalse(deletedMedicine.isPresent());
    }
}
