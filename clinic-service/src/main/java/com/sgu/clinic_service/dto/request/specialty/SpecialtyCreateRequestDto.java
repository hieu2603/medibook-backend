package com.sgu.clinic_service.dto.request.specialty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SpecialtyCreateRequestDto {
    @NotBlank(message = "Specialty name is required")
    @Size(max = 255, message = "Specialty name must not exceed 255 characters")
    private String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;
}
