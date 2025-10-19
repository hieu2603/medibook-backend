package com.sgu.clinic_service.service;

import com.sgu.clinic_service.dto.request.clinic.ClinicCreateRequestDto;
import com.sgu.clinic_service.dto.request.clinic.ClinicUpdateRequestDto;
import com.sgu.clinic_service.dto.response.clinic.ClinicResponseDto;
import com.sgu.clinic_service.dto.response.common.PaginationResponse;

import java.util.UUID;

public interface ClinicService {
    PaginationResponse<ClinicResponseDto> getAllClinics(String name, int page, int size);

    ClinicResponseDto getClinicById(UUID id);

    ClinicResponseDto createClinic(ClinicCreateRequestDto dto);

    ClinicResponseDto updateClinic(UUID id, ClinicUpdateRequestDto dto);
}
