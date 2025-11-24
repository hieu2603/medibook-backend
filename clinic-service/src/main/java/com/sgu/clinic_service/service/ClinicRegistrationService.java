package com.sgu.clinic_service.service;

import com.sgu.clinic_service.dto.common.PaginationResponse;
import com.sgu.clinic_service.dto.request.clinic.ClinicRegistrationCreateRequestDto;
import com.sgu.clinic_service.dto.response.clinic.ClinicRegistrationResponseDto;

import java.util.UUID;

public interface ClinicRegistrationService {
    ClinicRegistrationResponseDto createClinicRegistration(ClinicRegistrationCreateRequestDto dto);

    ClinicRegistrationResponseDto approveRegistration(UUID registrationId);

    ClinicRegistrationResponseDto rejectRegistration(UUID registrationId);

    PaginationResponse<ClinicRegistrationResponseDto> getRegistrations(int page, int size);
}
