package com.sgu.clinic_service.dto.request.doctor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class DoctorCreateRequestDto {
    @NotNull(message = "Clinic ID is required")
    private UUID clinicId;

    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    private String fullName;

    @NotNull(message = "Specialty ID is required")
    private UUID specialtyId;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^(?:\\+84|0)(?:3[2-9]|5[25689]|7[06-9]|8[1-9]|9[0-9])[0-9]{7}$|^(?:1800|1900)[0-9]{4,6}$",
            message = "Invalid phone number format"
    )
    private String phoneNumber;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;
}
