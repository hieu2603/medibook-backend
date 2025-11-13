package com.sgu.clinic_service.service;

import com.sgu.clinic_service.constant.DoctorStatus;
import com.sgu.clinic_service.dto.common.PaginationResponse;
import com.sgu.clinic_service.dto.request.doctor.DoctorCreateRequestDto;
import com.sgu.clinic_service.dto.request.doctor.DoctorUpdateRequestDto;
import com.sgu.clinic_service.dto.response.doctor.DoctorResponseDto;

import java.util.UUID;

public interface DoctorService {
    PaginationResponse<DoctorResponseDto> getAllDoctorsByClinicId(UUID clinicId, int page, int size);

    PaginationResponse<DoctorResponseDto> getAllDoctorsByClinicIdAndStatus(
            UUID clinicId, DoctorStatus status, int page, int size
    );

    DoctorResponseDto getDoctorById(UUID id);

    DoctorResponseDto createDoctor(DoctorCreateRequestDto dto, String role);

    DoctorResponseDto updateDoctor(UUID id, DoctorUpdateRequestDto dto, UUID userID, String role);

    DoctorResponseDto lockDoctor(UUID id, UUID userId, String role);

    DoctorResponseDto unlockDoctor(UUID id, UUID userId, String role);
}
