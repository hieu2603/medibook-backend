package com.sgu.clinic_service.repository;

import com.sgu.clinic_service.model.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty, UUID> {
    boolean existsByNameIgnoreCase(String name);
}
