package com.sgu.clinic_service.controller;

import com.sgu.clinic_service.constant.DoctorStatus;
import com.sgu.clinic_service.dto.request.doctor.DoctorCreateRequestDto;
import com.sgu.clinic_service.dto.request.doctor.DoctorUpdateRequestDto;
import com.sgu.clinic_service.dto.response.doctor.DoctorResponseDto;
import com.sgu.clinic_service.service.DoctorService;
import com.sgu.common.dto.ApiResponse;
import com.sgu.common.dto.PaginationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<DoctorResponseDto>>> getAllDoctorsByClinicId(
            @RequestParam UUID clinicId,
            @RequestParam(defaultValue = "ACTIVE", required = false) DoctorStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PaginationResponse<DoctorResponseDto> result;

        if (status != null) {
            result = doctorService.getAllDoctorsByClinicIdAndStatus(clinicId, status, page, size);
        } else {
            result = doctorService.getAllDoctorsByClinicId(clinicId, page, size);
        }

        ApiResponse<List<DoctorResponseDto>> response = ApiResponse.<List<DoctorResponseDto>>builder()
                .status(HttpStatus.OK.value())
                .message("Doctors retrieved successfully")
                .data(result.getData())
                .meta(result.getMeta())
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DoctorResponseDto>> getDoctorById(
            @PathVariable UUID id
    ) {
        DoctorResponseDto doctor = doctorService.getDoctorById(id);

        ApiResponse<DoctorResponseDto> response = ApiResponse.<DoctorResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message("Doctor retrieved successfully")
                .data(doctor)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DoctorResponseDto>> createDoctor(
            @Valid @RequestBody DoctorCreateRequestDto dto,
            @RequestHeader("X-User-Role") String role
    ) {
        DoctorResponseDto createdDoctor = doctorService.createDoctor(dto, role);

        ApiResponse<DoctorResponseDto> response = ApiResponse.<DoctorResponseDto>builder()
                .status(HttpStatus.CREATED.value())
                .message("Doctor created successfully")
                .data(createdDoctor)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DoctorResponseDto>> updateDoctor(
            @PathVariable UUID id,
            @Valid @RequestBody DoctorUpdateRequestDto dto,
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-User-Role") String role
    ) {
        DoctorResponseDto updatedDoctor = doctorService.updateDoctor(
                id,
                dto,
                UUID.fromString(userId),
                role
        );

        ApiResponse<DoctorResponseDto> response = ApiResponse.<DoctorResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message("Doctor updated successfully")
                .data(updatedDoctor)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PatchMapping("/{id}/lock")
    public ResponseEntity<ApiResponse<DoctorResponseDto>> lockDoctor(
            @PathVariable UUID id,
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-User-Role") String role
    ) {
        DoctorResponseDto doctor = doctorService.lockDoctor(id, UUID.fromString(userId), role);

        ApiResponse<DoctorResponseDto> response = ApiResponse.<DoctorResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message("Doctor locked successfully")
                .data(doctor)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PatchMapping("/{id}/unlock")
    public ResponseEntity<ApiResponse<DoctorResponseDto>> unlockDoctor(
            @PathVariable UUID id,
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-User-Role") String role
    ) {
        DoctorResponseDto doctor = doctorService.unlockDoctor(id, UUID.fromString(userId), role);

        ApiResponse<DoctorResponseDto> response = ApiResponse.<DoctorResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message("Doctor unlocked successfully")
                .data(doctor)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
