package com.aldiramdan.hospital.repository;

import com.aldiramdan.hospital.model.entity.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreatmentRepository extends JpaRepository<Treatment, String> {
    @Query("""
            select t from Treatment t inner join Patient p\s
            on t.patient.id = p.id\s
            where p.name like %:name%\s
            """)
    List<Treatment> findByPatientName(String name);
}
