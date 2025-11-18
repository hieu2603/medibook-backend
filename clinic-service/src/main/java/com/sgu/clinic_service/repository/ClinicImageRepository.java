package com.sgu.clinic_service.repository;

import com.sgu.clinic_service.model.ClinicImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClinicImageRepository extends JpaRepository<ClinicImage, UUID> {
    List<ClinicImage> findByClinicId(UUID clinicId);
}
