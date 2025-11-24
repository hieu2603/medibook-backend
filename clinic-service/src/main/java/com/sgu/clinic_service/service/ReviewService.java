package com.sgu.clinic_service.service;

import com.sgu.clinic_service.dto.common.PaginationResponse;
import com.sgu.clinic_service.dto.request.review.ReviewCreateRequestDto;
import com.sgu.clinic_service.dto.response.review.ReviewResponseDto;

import java.util.UUID;

public interface ReviewService {
    ReviewResponseDto createReview(UUID clinicId, ReviewCreateRequestDto dto);

    PaginationResponse<ReviewResponseDto> getReviewsByClinic(
            UUID clinicId,
            int page, int size
    );

    PaginationResponse<ReviewResponseDto> getReviewsByPatient(
            UUID patientId,
            int page, int size
    );

    void deleteReview(UUID reviewId);
}
