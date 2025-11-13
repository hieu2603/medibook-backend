package com.sgu.user_service.security;

import com.sgu.user_service.constant.Role;
import com.sgu.user_service.exception.AccessDeniedException;
import com.sgu.user_service.model.User;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserPermissionValidator {

    public void validateGetUsersPermission(String role) {
        boolean isAdmin = Role.ADMIN.name().equalsIgnoreCase(role);

        if (!isAdmin) {
            throw new AccessDeniedException("You are not allowed to get users");
        }
    }

    public void validateGetUserByIdPermission(User user, UUID id, String role) {
        boolean isAdmin = Role.ADMIN.name().equalsIgnoreCase(role);
        boolean isOwnerUser = user.getId().equals(id);

        if (!isAdmin && !isOwnerUser) {
            throw new AccessDeniedException("You are not allowed to get this user");
        }
    }

    public void validateUpdateAvatarPermission(User user, UUID id) {
        boolean isOwnerUser = user.getId().equals(id);

        if (!isOwnerUser) {
            throw new AccessDeniedException("You are not allowed to update avatar for this user");
        }
    }
}
