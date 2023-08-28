package com.aldiramdan.hospital.repository;

import com.aldiramdan.hospital.model.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, String> {
    List<Patient> findByNameContaining(String name);
}
