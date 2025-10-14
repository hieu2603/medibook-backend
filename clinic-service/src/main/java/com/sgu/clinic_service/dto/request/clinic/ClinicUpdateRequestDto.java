package com.sgu.clinic_service.dto.request.clinic;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClinicUpdateRequestDto {
    @Size(max = 255, message = "Clinic name must not exceed 255 characters")
    private String clinicName;

    @Pattern(
            regexp = "^(?:\\+84|0)(?:3[2-9]|5[25689]|7[06-9]|8[1-9]|9[0-9])[0-9]{7}$|^(?:1800|1900)[0-9]{4,6}$",
            message = "Invalid phone number format"
    )
    private String phone;

    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;

    @DecimalMin(value = "-90.0", message = "Latitude must be greater than or equal to -90")
    @DecimalMax(value = "90.0", message = "Latitude must be less than or equal to 90")
    private BigDecimal latitude;

    @DecimalMin(value = "-180.0", message = "Longitude must be greater than or equal to -180")
    @DecimalMax(value = "180.0", message = "Longitude must be less than or equal to 180")
    private BigDecimal longitude;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @PositiveOrZero(message = "Price must be a positive number")
    private BigDecimal price;
}
