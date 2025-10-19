package com.sgu.clinic_service.controller;

import com.sgu.clinic_service.dto.request.clinic.ClinicCreateRequestDto;
import com.sgu.clinic_service.dto.request.clinic.ClinicUpdateRequestDto;
import com.sgu.clinic_service.dto.response.clinic.ClinicResponseDto;
import com.sgu.clinic_service.dto.response.common.ApiResponse;
import com.sgu.clinic_service.dto.response.common.PaginationResponse;
import com.sgu.clinic_service.service.ClinicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/clinics")
@RequiredArgsConstructor
public class ClinicController {
    private final ClinicService clinicService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClinicResponseDto>>> getClinics(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PaginationResponse<ClinicResponseDto> result = clinicService.getClinics(name, page, size);

        ApiResponse<List<ClinicResponseDto>> response = ApiResponse.<List<ClinicResponseDto>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
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
                .success(true)
                .message("Clinic retrieved successfully")
                .data(clinic)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ClinicResponseDto>> createClinic(
            @Valid @RequestBody ClinicCreateRequestDto requestDto
    ) {
        ClinicResponseDto createdClinic = clinicService.createClinic(requestDto);

        ApiResponse<ClinicResponseDto> response = ApiResponse.<ClinicResponseDto>builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
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
            @RequestBody ClinicUpdateRequestDto requestDto
    ) {
        ClinicResponseDto updatedClinic = clinicService.updateClinic(id, requestDto);

        ApiResponse<ClinicResponseDto> response = ApiResponse.<ClinicResponseDto>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Clinic updated successfully")
                .data(updatedClinic)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
