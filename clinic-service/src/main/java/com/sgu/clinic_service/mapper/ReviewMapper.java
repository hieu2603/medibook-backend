package com.sgu.clinic_service.mapper;

import com.sgu.clinic_service.dto.request.review.ReviewCreateRequestDto;
import com.sgu.clinic_service.dto.response.review.ReviewResponseDto;
import com.sgu.clinic_service.model.Review;

public class ReviewMapper {

    // Từ Create DTO -> Entity
    public static Review toEntity(ReviewCreateRequestDto dto) {
        return Review.builder()
                .rating(dto.getRating())
                .comment(dto.getComment())
                .appointmentId(dto.getAppointmentId())
                .patientId(dto.getPatientId())
                .clinicId(dto.getClinicId())
                .build();
    }

    public static ReviewResponseDto toDto(Review review) {
        return ReviewResponseDto.builder()
                .reviewId(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .appointmentId(review.getAppointmentId())
                .patientId(review.getPatientId())
                .clinicId(review.getClinicId())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
