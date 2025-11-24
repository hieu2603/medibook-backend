package com.sgu.clinic_service.dto.response.review;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class ReviewResponseDto {
    private UUID reviewId;
    private Integer rating;
    private String comment;
    private UUID appointmentId;
    private UUID patientId;
    private UUID clinicId;
    private LocalDate createdAt;
}
