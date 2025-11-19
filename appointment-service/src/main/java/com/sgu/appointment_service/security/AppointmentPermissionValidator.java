package com.sgu.appointment_service.security;

import com.sgu.appointment_service.exception.AccessDeniedException;
import com.sgu.appointment_service.model.Appointment;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AppointmentPermissionValidator {

    public void validateCreatePermission(UUID patientUserId, UUID userId, String role) {
        boolean isPatient = "PATIENT".equalsIgnoreCase(role);
        boolean isOwner = patientUserId.equals(userId);

        if (!isPatient || !isOwner) {
            throw new AccessDeniedException("Only patients can create appointments for themselves");
        }
    }

    public void validateUpdatePermission(Appointment appointment, UUID patientUserId, UUID clinicUserId, UUID userId,
            String role) {
        boolean isAdmin = "ADMIN".equalsIgnoreCase(role);
        boolean isOwnerPatient = "PATIENT".equalsIgnoreCase(role) && patientUserId.equals(userId);
        boolean isOwnerClinic = "CLINIC".equalsIgnoreCase(role) && clinicUserId.equals(userId);

        if (!isAdmin && !isOwnerPatient && !isOwnerClinic) {
            throw new AccessDeniedException("You are not allowed to update this appointment");
        }
    }

    public void validateCancelPermission(Appointment appointment, UUID patientUserId, UUID clinicUserId, UUID userId,
            String role) {
        boolean isAdmin = "ADMIN".equalsIgnoreCase(role);
        boolean isOwnerPatient = "PATIENT".equalsIgnoreCase(role) && patientUserId.equals(userId);
        boolean isOwnerClinic = "CLINIC".equalsIgnoreCase(role) && clinicUserId.equals(userId);

        if (!isAdmin && !isOwnerPatient && !isOwnerClinic) {
            throw new AccessDeniedException("You are not allowed to cancel this appointment");
        }
    }

    public void validateViewPermission(Appointment appointment, UUID patientUserId, UUID clinicUserId, UUID userId,
            String role) {
        boolean isAdmin = "ADMIN".equalsIgnoreCase(role);
        boolean isOwnerPatient = "PATIENT".equalsIgnoreCase(role) && patientUserId.equals(userId);
        boolean isOwnerClinic = "CLINIC".equalsIgnoreCase(role) && clinicUserId.equals(userId);

        if (!isAdmin && !isOwnerPatient && !isOwnerClinic) {
            throw new AccessDeniedException("You are not allowed to view this appointment");
        }
    }

    public void validateDeletePermission(Appointment appointment, UUID patientUserId, UUID clinicUserId, UUID userId,
            String role) {
        boolean isAdmin = "ADMIN".equalsIgnoreCase(role);
        boolean isOwnerPatient = "PATIENT".equalsIgnoreCase(role) && patientUserId.equals(userId);
        boolean isOwnerClinic = "CLINIC".equalsIgnoreCase(role) && clinicUserId.equals(userId);

        if (!isAdmin && !isOwnerPatient && !isOwnerClinic) {
            throw new AccessDeniedException("You are not allowed to delete this appointment");
        }
    }
}
