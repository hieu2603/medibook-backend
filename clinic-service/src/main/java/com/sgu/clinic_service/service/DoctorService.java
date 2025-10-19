package com.sgu.clinic_service.service;

import com.sgu.clinic_service.constant.DoctorStatus;
import com.sgu.clinic_service.dto.request.doctor.DoctorCreateRequestDto;
import com.sgu.clinic_service.dto.request.doctor.DoctorUpdateRequestDto;
import com.sgu.clinic_service.dto.response.common.PaginationResponse;
import com.sgu.clinic_service.dto.response.doctor.DoctorResponseDto;

import java.util.UUID;

public interface DoctorService {
    PaginationResponse<DoctorResponseDto> getAllDoctorsByClinicId(UUID clinicId, int page, int size);

    PaginationResponse<DoctorResponseDto> getAllDoctorsByClinicIdAndStatus(
            UUID clinicId, DoctorStatus status, int page, int size
    );

    DoctorResponseDto getDoctorById(UUID id);

    DoctorResponseDto createDoctor(DoctorCreateRequestDto dto);

    DoctorResponseDto updateDoctor(UUID id, DoctorUpdateRequestDto dto);

    DoctorResponseDto lockDoctor(UUID id);

    DoctorResponseDto unlockDoctor(UUID id);
}
