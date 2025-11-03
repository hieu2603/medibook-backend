package com.sgu.clinic_service.security;

import com.sgu.clinic_service.model.Clinic;
import com.sgu.common.exception.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ClinicPermissionValidator {

    public void validateUpdatePermission(Clinic clinic, UUID userId, String role) {
        boolean isAdmin = "ADMIN".equalsIgnoreCase(role);
        boolean isOwnerClinic = "CLINIC".equalsIgnoreCase(role) && clinic.getUserId().equals(userId);

        if (!isAdmin && !isOwnerClinic) {
            throw new AccessDeniedException("You are not allowed to update this clinic");
        }
    }
}
