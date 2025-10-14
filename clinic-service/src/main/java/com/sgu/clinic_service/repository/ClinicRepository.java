package com.sgu.clinic_service.repository;

import com.sgu.clinic_service.model.Clinic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClinicRepository extends JpaRepository<Clinic, UUID> {
    // SELECT * FROM clinics WHERE LOWER(clinic_name) LIKE LOWER('%clinic%')
    Page<Clinic> findByClinicNameContainingIgnoreCase(String name, Pageable pageable);
}
