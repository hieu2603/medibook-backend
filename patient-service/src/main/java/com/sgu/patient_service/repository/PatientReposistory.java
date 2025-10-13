package com.sgu.patient_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.UUID;

import com.sgu.patient_service.model.Patient;

public interface PatientReposistory extends JpaRepository<Patient, UUID> {

        @Query("SELECT p FROM Patient p WHERE " +
                        "(:name IS NULL OR LOWER(p.full_name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
                        "(:phone IS NULL OR p.phone LIKE CONCAT('%', :phone, '%'))")
        Page<Patient> findByNameAndPhone(@Param("name") String name,
                        @Param("phone") String phone,
                        Pageable pageable);
}
