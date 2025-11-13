package com.sgu.clinic_service.security;

import com.sgu.clinic_service.exception.AccessDeniedException;
import com.sgu.clinic_service.model.Clinic;
import com.sgu.clinic_service.model.Doctor;
import com.sgu.clinic_service.repository.ClinicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DoctorPermissionValidator {

    private final ClinicRepository clinicRepository;

    public void validateCreatePermission(String role) {
        boolean isClinic = "CLINIC".equalsIgnoreCase(role);

        if (!isClinic) {
            throw new AccessDeniedException("You are not allowed to create doctor");
        }
    }

    public void validateUpdatePermission(Doctor doctor, UUID userId, String role) {
        boolean isOwnerClinic = "CLINIC".equalsIgnoreCase(role) && clinicRepository.findByUserId(userId)
                .map(Clinic::getId)
                .filter(ownedClinicId -> ownedClinicId.equals(doctor.getClinicId()))
                .isPresent();

        if (!isOwnerClinic) {
            throw new AccessDeniedException("You are not allowed to create doctor");
        }
    }

    public void validateLockPermission(Doctor doctor, UUID userId, String role) {
        boolean isOwnerClinic = "CLINIC".equalsIgnoreCase(role) && clinicRepository.findByUserId(userId)
                .map(Clinic::getId)
                .filter(ownedClinicId -> ownedClinicId.equals(doctor.getClinicId()))
                .isPresent();

        if (!isOwnerClinic) {
            throw new AccessDeniedException("You are not allowed to lock this doctor");
        }
    }

    public void validateUnlockPermission(Doctor doctor, UUID userId, String role) {
        boolean isOwnerClinic = "CLINIC".equalsIgnoreCase(role) && clinicRepository.findByUserId(userId)
                .map(Clinic::getId)
                .filter(ownedClinicId -> ownedClinicId.equals(doctor.getClinicId()))
                .isPresent();

        if (!isOwnerClinic) {
            throw new AccessDeniedException("You are not allowed to unlock this doctor");
        }
    }
}
