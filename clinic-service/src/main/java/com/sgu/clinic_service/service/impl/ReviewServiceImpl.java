package com.sgu.clinic_service.service.impl;

import com.sgu.clinic_service.dto.common.PaginationMeta;
import com.sgu.clinic_service.dto.common.PaginationResponse;
import com.sgu.clinic_service.dto.request.review.ReviewCreateRequestDto;
import com.sgu.clinic_service.dto.response.review.ReviewResponseDto;
import com.sgu.clinic_service.exception.ResourceNotFoundException;
import com.sgu.clinic_service.mapper.ReviewMapper;
import com.sgu.clinic_service.model.Review;
import com.sgu.clinic_service.repository.ReviewRepository;
import com.sgu.clinic_service.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    public ReviewResponseDto createReview(UUID clinicId, ReviewCreateRequestDto dto) {
        // TODO: Kiểm tra điều kiện appointment

        // ...
        Review review = ReviewMapper.toEntity(dto);

        Review newReview = reviewRepository.save(review);

        return ReviewMapper.toDto(newReview);
    }

    @Override
    public PaginationResponse<ReviewResponseDto> getReviewsByClinic(UUID clinicId, int page, int size) {
        int pageIndex = (page <= 0) ? 0 : page - 1;

        Pageable pageable = PageRequest.of(pageIndex, size, Sort.by("createdAt").descending());

        Page<Review> reviewPage = reviewRepository.findByClinicId(clinicId, pageable);

        List<ReviewResponseDto> data = reviewPage.map(ReviewMapper::toDto).getContent();

        long totalItems = reviewPage.getTotalElements();

        PaginationMeta meta = PaginationMeta.builder()
                .currentPage(totalItems == 0 ? 0 : reviewPage.getNumber() + 1)
                .pageSize(reviewPage.getSize())
                .totalPages(reviewPage.getTotalPages())
                .totalItems(totalItems)
                .build();

        return PaginationResponse.<ReviewResponseDto>builder()
                .meta(meta)
                .data(data)
                .build();
    }

    @Override
    public PaginationResponse<ReviewResponseDto> getReviewsByPatient(UUID patientId, int page, int size) {
        int pageIndex = (page <= 0) ? 0 : page - 1;

        Pageable pageable = PageRequest.of(pageIndex, size, Sort.by("createdAt").descending());

        Page<Review> reviewPage = reviewRepository.findByPatientId(patientId, pageable);

        List<ReviewResponseDto> data = reviewPage.map(ReviewMapper::toDto).getContent();

        long totalItems = reviewPage.getTotalElements();

        PaginationMeta meta = PaginationMeta.builder()
                .currentPage(totalItems == 0 ? 0 : reviewPage.getNumber() + 1)
                .pageSize(reviewPage.getSize())
                .totalPages(reviewPage.getTotalPages())
                .totalItems(totalItems)
                .build();

        return PaginationResponse.<ReviewResponseDto>builder()
                .meta(meta)
                .data(data)
                .build();
    }

    @Override
    public void deleteReview(UUID reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review %s not found".formatted(reviewId)));

        reviewRepository.delete(review);
    }
}
