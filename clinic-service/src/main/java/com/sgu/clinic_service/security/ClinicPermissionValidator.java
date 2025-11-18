package com.sgu.clinic_service.security;

import com.sgu.clinic_service.exception.AccessDeniedException;
import com.sgu.clinic_service.exception.ResourceNotFoundException;
import com.sgu.clinic_service.model.Clinic;
import com.sgu.clinic_service.model.ClinicImage;
import com.sgu.clinic_service.repository.ClinicImageRepository;
import com.sgu.clinic_service.repository.ClinicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ClinicPermissionValidator {

    private final ClinicRepository clinicRepository;
    private final ClinicImageRepository clinicImageRepository;

    public void validateUpdatePermission(Clinic clinic, UUID userId, String role) {
        boolean isAdmin = "ADMIN".equalsIgnoreCase(role);
        boolean isOwnerClinic = "CLINIC".equalsIgnoreCase(role) && clinic.getUserId().equals(userId);

        if (!isAdmin && !isOwnerClinic) {
            throw new AccessDeniedException("You are not allowed to update this clinic");
        }
    }

    public void validateUploadImagePermission(
            UUID clinicId, UUID userId, String role
    ) {
        Clinic clinic = clinicRepository.findById(clinicId)
                .orElseThrow(() -> new ResourceNotFoundException("Clinic not found"));


        boolean isOwnerClinic = "CLINIC".equalsIgnoreCase(role) && clinic.getUserId().equals(userId);

        if (!isOwnerClinic) {
            throw new AccessDeniedException("You are not allowed to upload image");
        }
    }

    public void validateDeleteImagePermission(
            UUID imageId, UUID userId, String role
    ) {
        ClinicImage image = clinicImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found"));

        Clinic clinic = clinicRepository.findById(image.getClinicId())
                .orElseThrow(() -> new ResourceNotFoundException("Clinic not found"));

        boolean isOwnerClinic = "CLINIC".equalsIgnoreCase(role) && clinic.getUserId().equals(userId);

        if (!isOwnerClinic) {
            throw new AccessDeniedException("You are not allowed to delete image");
        }
    }
}
