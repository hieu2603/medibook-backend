package com.sgu.clinic_service.security;

import com.sgu.clinic_service.exception.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
public class SpecialtyPermissionValidator {
    public void validateCreatePermission(String role) {
        boolean isAdmin = "ADMIN".equalsIgnoreCase(role);

        if (!isAdmin) {
            throw new AccessDeniedException("You are not allowed to create specialty");
        }
    }

    public void validateUpdatePermission(String role) {
        boolean isAdmin = "ADMIN".equalsIgnoreCase(role);

        if (!isAdmin) {
            throw new AccessDeniedException("You are not allowed to update this specialty");
        }
    }
}
