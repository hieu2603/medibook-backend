package com.sgu.clinic_service.controller;

import com.sgu.clinic_service.dto.common.ApiResponse;
import com.sgu.clinic_service.dto.common.PaginationResponse;
import com.sgu.clinic_service.dto.request.clinic.ClinicCreateRequestDto;
import com.sgu.clinic_service.dto.request.clinic.ClinicUpdateRequestDto;
import com.sgu.clinic_service.dto.response.clinic.ClinicResponseDto;
import com.sgu.clinic_service.service.ClinicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/clinics")
@RequiredArgsConstructor
@Slf4j
public class ClinicController {
    private final ClinicService clinicService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClinicResponseDto>>> getClinics(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            @RequestParam(required = false) Double radius,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PaginationResponse<ClinicResponseDto> result = clinicService.getClinics(
                name, latitude, longitude, radius, page, size
        );

        ApiResponse<List<ClinicResponseDto>> response = ApiResponse.<List<ClinicResponseDto>>builder()
                .status(HttpStatus.OK.value())
                .message("Clinics retrieved successfully")
                .data(result.getData())
                .meta(result.getMeta())
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClinicResponseDto>> getClinicById(
            @PathVariable UUID id
    ) {
        ClinicResponseDto clinic = clinicService.getClinicById(id);

        ApiResponse<ClinicResponseDto> response = ApiResponse.<ClinicResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message("Clinic retrieved successfully")
                .data(clinic)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ClinicResponseDto>> createClinic(
            @Valid @RequestBody ClinicCreateRequestDto dto
    ) {
        ClinicResponseDto createdClinic = clinicService.createClinic(dto);

        ApiResponse<ClinicResponseDto> response = ApiResponse.<ClinicResponseDto>builder()
                .status(HttpStatus.CREATED.value())
                .message("Clinic created successfully")
                .data(createdClinic)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClinicResponseDto>> updateClinic(
            @PathVariable UUID id,
            @RequestBody ClinicUpdateRequestDto dto,
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-User-Role") String role
    ) {
        ClinicResponseDto updatedClinic = clinicService.updateClinic(
                id,
                dto,
                UUID.fromString(userId),
                role
        );

        ApiResponse<ClinicResponseDto> response = ApiResponse.<ClinicResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message("Clinic updated successfully")
                .data(updatedClinic)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
