package com.sgu.auth_service.dto.request.patient;

import com.sgu.auth_service.constant.PatientGender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class PatientCreateRequest {
    @NotBlank(message = "Full name is required")
    @Size(max = 255, message = "Full name must not exceed 255 characters")
    private String fullName;

    @NotBlank(message = "Phone is required")
    private String phone;

    private LocalDate dob;

    private PatientGender gender;

    private String address;

    @NotNull(message = "User ID is required")
    private UUID userId;
}
