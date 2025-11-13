package com.sgu.auth_service.security;

import com.sgu.auth_service.constant.Role;
import com.sgu.auth_service.exception.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
public class AuthPermissionValidator {

    public void validateLockPermission(String role) {
        boolean isAdmin = Role.ADMIN.name().equalsIgnoreCase(role);

        if (!isAdmin) {
            throw new AccessDeniedException("You are not allowed lock this user");
        }
    }

    public void validateUnlockPermission(String role) {
        boolean isAdmin = Role.ADMIN.name().equalsIgnoreCase(role);

        if (!isAdmin) {
            throw new AccessDeniedException("You are not allowed unlock this user");
        }
    }
}
