package com.sgu.clinic_service.service;

import com.sgu.clinic_service.dto.request.clinic.ClinicCreateRequestDto;
import com.sgu.clinic_service.dto.request.clinic.ClinicUpdateRequestDto;
import com.sgu.clinic_service.dto.response.clinic.ClinicResponseDto;

import java.util.List;
import java.util.UUID;

public interface ClinicService {
    List<ClinicResponseDto> getAllClinics();

    ClinicResponseDto getClinicById(UUID id);

    ClinicResponseDto createClinic(ClinicCreateRequestDto requestDto);

    ClinicResponseDto updateClinic(UUID id, ClinicUpdateRequestDto requestDto);

    List<ClinicResponseDto> searchClinicsByName(String keyword);
}
