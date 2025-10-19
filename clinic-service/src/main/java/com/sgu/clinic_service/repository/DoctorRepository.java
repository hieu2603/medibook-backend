package com.sgu.clinic_service.repository;

import com.sgu.clinic_service.constant.DoctorStatus;
import com.sgu.clinic_service.model.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, UUID> {
    Page<Doctor> findAllByClinicId(UUID clinicId, Pageable pageable);

    Page<Doctor> findAllByClinicIdAndStatus(UUID clinicId, DoctorStatus status, Pageable pageable);
}
