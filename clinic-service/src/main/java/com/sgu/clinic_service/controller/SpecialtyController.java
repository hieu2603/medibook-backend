package com.sgu.clinic_service.controller;

import com.sgu.clinic_service.dto.request.specialty.SpecialtyCreateRequestDto;
import com.sgu.clinic_service.dto.request.specialty.SpecialtyUpdateRequestDto;
import com.sgu.clinic_service.dto.response.common.ApiResponse;
import com.sgu.clinic_service.dto.response.specialty.SpecialtyResponseDto;
import com.sgu.clinic_service.service.SpecialtyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/specialties")
@RequiredArgsConstructor
public class SpecialtyController {
    private final SpecialtyService specialtyService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SpecialtyResponseDto>>> getAllSpecialties() {
        List<SpecialtyResponseDto> specialties = specialtyService.getAllSpecialties();

        ApiResponse<List<SpecialtyResponseDto>> response = ApiResponse.<List<SpecialtyResponseDto>>builder()
                .status(HttpStatus.OK.value())
                .message("Specialties retrieved successfully")
                .data(specialties)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SpecialtyResponseDto>> getSpecialtyById(
            @PathVariable UUID id
    ) {
        SpecialtyResponseDto specialty = specialtyService.getSpecialtyById(id);

        ApiResponse<SpecialtyResponseDto> response = ApiResponse.<SpecialtyResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message("Specialty retrieved successfully")
                .data(specialty)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SpecialtyResponseDto>> createSpecialty(
            @Valid @RequestBody SpecialtyCreateRequestDto dto
    ) {
        SpecialtyResponseDto createdSpecialty = specialtyService.createSpecialty(dto);

        ApiResponse<SpecialtyResponseDto> response = ApiResponse.<SpecialtyResponseDto>builder()
                .status(HttpStatus.CREATED.value())
                .message("Specialty created successfully")
                .data(createdSpecialty)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SpecialtyResponseDto>> updateSpecialty(
            @PathVariable UUID id,
            @Valid @RequestBody SpecialtyUpdateRequestDto dto
    ) {
        SpecialtyResponseDto updatedSpecialty = specialtyService.updateSpecialty(id, dto);

        ApiResponse<SpecialtyResponseDto> response = ApiResponse.<SpecialtyResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message("Specialty updated successfully")
                .data(updatedSpecialty)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
