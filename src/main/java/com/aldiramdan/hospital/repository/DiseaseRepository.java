package com.aldiramdan.hospital.repository;

import com.aldiramdan.hospital.model.entity.Disease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiseaseRepository extends JpaRepository<Disease, String> {
    List<Disease> findByNameContaining(String name);
    Optional<Disease> findByName(String name);
}
