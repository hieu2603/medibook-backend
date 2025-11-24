package com.sgu.clinic_service.controller;

import com.sgu.clinic_service.dto.common.ApiResponse;
import com.sgu.clinic_service.dto.common.PaginationResponse;
import com.sgu.clinic_service.dto.request.clinic.ClinicRegistrationCreateRequestDto;
import com.sgu.clinic_service.dto.response.clinic.ClinicRegistrationResponseDto;
import com.sgu.clinic_service.service.ClinicRegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/clinic-registrations")
@RequiredArgsConstructor
public class ClinicRegistrationController {

    private final ClinicRegistrationService clinicRegistrationService;

    @PostMapping
    public ResponseEntity<ApiResponse<ClinicRegistrationResponseDto>> createClinicRegistration(
            @Valid @RequestBody ClinicRegistrationCreateRequestDto dto
    ) {
        ClinicRegistrationResponseDto clinicRegistration = clinicRegistrationService
                .createClinicRegistration(dto);

        ApiResponse<ClinicRegistrationResponseDto> response = ApiResponse.<ClinicRegistrationResponseDto>builder()
                .status(HttpStatus.CREATED.value())
                .message("Clinic registration submitted successfully")
                .data(clinicRegistration)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<ClinicRegistrationResponseDto>> approveRegistration(
            @PathVariable UUID id
    ) {
        ClinicRegistrationResponseDto clinicRegistration = clinicRegistrationService
                .approveRegistration(id);

        ApiResponse<ClinicRegistrationResponseDto> response = ApiResponse.<ClinicRegistrationResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message("Clinic registration approved successfully")
                .data(clinicRegistration)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<ClinicRegistrationResponseDto>> rejectRegistration(
            @PathVariable UUID id
    ) {
        ClinicRegistrationResponseDto clinicRegistration = clinicRegistrationService
                .rejectRegistration(id);

        ApiResponse<ClinicRegistrationResponseDto> response = ApiResponse.<ClinicRegistrationResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message("Clinic registration rejected successfully")
                .data(clinicRegistration)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    // Mặc định sort theo desc (giảm dần theo createdAt)
    @GetMapping
    public ResponseEntity<ApiResponse<List<ClinicRegistrationResponseDto>>> getRegistration(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PaginationResponse<ClinicRegistrationResponseDto> result = clinicRegistrationService
                .getRegistrations(page, size);

        ApiResponse<List<ClinicRegistrationResponseDto>> response = ApiResponse.<List<ClinicRegistrationResponseDto>>builder()
                .status(HttpStatus.OK.value())
                .message("Clinic registration retrieved successfully")
                .data(result.getData())
                .meta(result.getMeta())
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
