package com.sgu.clinic_service.service;

import com.sgu.clinic_service.dto.request.specialty.SpecialtyCreateRequestDto;
import com.sgu.clinic_service.dto.request.specialty.SpecialtyUpdateRequestDto;
import com.sgu.clinic_service.dto.response.specialty.SpecialtyResponseDto;

import java.util.List;
import java.util.UUID;

public interface SpecialtyService {
    List<SpecialtyResponseDto> getAllSpecialties();

    SpecialtyResponseDto getSpecialtyById(UUID id);

    SpecialtyResponseDto createSpecialty(SpecialtyCreateRequestDto dto);

    SpecialtyResponseDto updateSpecialty(UUID id, SpecialtyUpdateRequestDto dto);
}
