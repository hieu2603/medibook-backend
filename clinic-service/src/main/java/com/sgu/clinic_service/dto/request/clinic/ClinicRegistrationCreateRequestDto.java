package com.sgu.clinic_service.dto.request.clinic;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ClinicRegistrationCreateRequestDto {

    @NotBlank(message = "Clinic name is required")
    private String clinicName;

    @NotBlank(message = "Email is required")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone is required")
    private String phone;

    // Optional
    private String address;

    // Optional
    private BigDecimal latitude;

    // Optional
    private BigDecimal longitude;

    // Optional
    private String description;

    // Optional
    private BigDecimal price;
}
