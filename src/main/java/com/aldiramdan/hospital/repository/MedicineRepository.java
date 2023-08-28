package com.aldiramdan.hospital.repository;

import com.aldiramdan.hospital.model.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, String> {
    List<Medicine> findByNameContaining(String name);
    Optional<Medicine> findByName(String name);
}
