package com.sgu.patient_service.repository;

import com.sgu.patient_service.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface PatientRepository extends JpaRepository<Patient, UUID> {

    @Query("SELECT p FROM Patient p WHERE LOWER(p.fullName) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Patient> findByName(@Param("name") String name, Pageable pageable);

    @Query("SELECT p FROM Patient p WHERE p.phone IS NOT NULL AND p.phone LIKE CONCAT('%', :phone, '%')")
    Page<Patient> findByPhone(@Param("phone") String phone, Pageable pageable);

    @Query("""
                SELECT p FROM Patient p
                WHERE (:name IS NOT NULL AND LOWER(p.fullName) LIKE LOWER(CONCAT('%', :name, '%')))
                   OR (:phone IS NOT NULL AND p.phone IS NOT NULL AND p.phone LIKE CONCAT('%', :phone, '%'))
            """)
    Page<Patient> findByNameOrPhone(@Param("name") String name,
                                    @Param("phone") String phone,
                                    Pageable pageable);
}
