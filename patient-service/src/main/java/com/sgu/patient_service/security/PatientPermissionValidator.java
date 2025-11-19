package com.sgu.patient_service.security;

import com.sgu.patient_service.exception.AccessDeniedException;
import com.sgu.patient_service.model.Patient;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PatientPermissionValidator {

    public void validateUpdatePatientPermission(Patient patient, UUID userId, String role) {
        boolean isOwnerPatient = "PATIENT".equalsIgnoreCase(role) && patient.getUserId().equals(userId);

        if (!isOwnerPatient) {
            throw new AccessDeniedException("You are not allowed to update this patient");
        }
    }
}
