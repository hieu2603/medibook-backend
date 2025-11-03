package com.sgu.clinic_service.dto.request.clinic;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ClinicCreateRequestDto {
    @NotBlank(message = "Clinic name is required")
    @Size(max = 255, message = "Clinic name must not exceed 255 characters")
    private String clinicName;

    @NotNull(message = "User ID is required")
    private UUID userId;
}
