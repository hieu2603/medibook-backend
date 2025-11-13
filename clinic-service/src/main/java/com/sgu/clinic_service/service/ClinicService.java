package com.sgu.clinic_service.service;

import com.sgu.clinic_service.dto.common.PaginationResponse;
import com.sgu.clinic_service.dto.request.clinic.ClinicCreateRequestDto;
import com.sgu.clinic_service.dto.request.clinic.ClinicUpdateRequestDto;
import com.sgu.clinic_service.dto.response.clinic.ClinicResponseDto;

import java.util.UUID;

public interface ClinicService {
    PaginationResponse<ClinicResponseDto> getClinics(
            String name, Double latitude, Double longitude, Double radius,
            int page, int size
    );

    ClinicResponseDto getClinicById(UUID id);

    ClinicResponseDto createClinic(ClinicCreateRequestDto dto);

    ClinicResponseDto updateClinic(UUID id, ClinicUpdateRequestDto dto, UUID userId, String role);
}
