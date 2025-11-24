package com.sgu.clinic_service.dto.request.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ReviewCreateRequestDto {

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating can not exceed 5")
    private Integer rating;

    private String comment; // Optional không truyền cũng được

    @NotNull(message = "Appointment ID is required")
    private UUID appointmentId;

    @NotNull(message = "Patient ID is required")
    private UUID patientId;

    @NotNull(message = "Clinic ID is required")
    private UUID clinicId;
}
