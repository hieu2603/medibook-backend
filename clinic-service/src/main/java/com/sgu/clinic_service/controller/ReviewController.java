package com.sgu.clinic_service.controller;

import com.sgu.clinic_service.dto.common.ApiResponse;
import com.sgu.clinic_service.dto.common.PaginationResponse;
import com.sgu.clinic_service.dto.request.review.ReviewCreateRequestDto;
import com.sgu.clinic_service.dto.response.review.ReviewResponseDto;
import com.sgu.clinic_service.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{clinicId}")
    public ResponseEntity<ApiResponse<ReviewResponseDto>> createReview(
            @PathVariable UUID clinicId,
            @Valid @RequestBody ReviewCreateRequestDto dto
    ) {
        ReviewResponseDto newReview = reviewService.createReview(clinicId, dto);

        ApiResponse<ReviewResponseDto> response = ApiResponse.<ReviewResponseDto>builder()
                .status(HttpStatus.CREATED.value())
                .message("Review created successfully")
                .data(newReview)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }

    @GetMapping("/clinic/{clinicId}")
    public ResponseEntity<ApiResponse<List<ReviewResponseDto>>> getReviewsByClinic(
            @PathVariable UUID clinicId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PaginationResponse<ReviewResponseDto> result = reviewService
                .getReviewsByClinic(clinicId, page, size);

        ApiResponse<List<ReviewResponseDto>> response = ApiResponse.<List<ReviewResponseDto>>builder()
                .status(HttpStatus.OK.value())
                .message("Fetched reviews of clinic %s successfully".formatted(clinicId))
                .data(result.getData())
                .meta(result.getMeta())
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<ApiResponse<List<ReviewResponseDto>>> getReviewsByPatient(
            @PathVariable UUID patientId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PaginationResponse<ReviewResponseDto> result = reviewService
                .getReviewsByPatient(patientId, page, size);

        ApiResponse<List<ReviewResponseDto>> response = ApiResponse.<List<ReviewResponseDto>>builder()
                .status(HttpStatus.OK.value())
                .message("Fetched reviews of patient %s successfully".formatted(patientId))
                .data(result.getData())
                .meta(result.getMeta())
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(
            @PathVariable UUID id
    ) {
        reviewService.deleteReview(id);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Review %s deleted successfully".formatted(id))
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
