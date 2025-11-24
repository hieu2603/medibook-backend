package com.sgu.clinic_service.repository;

import com.sgu.clinic_service.model.ClinicRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClinicRegistrationRepository extends JpaRepository<ClinicRegistration, UUID> {
}
