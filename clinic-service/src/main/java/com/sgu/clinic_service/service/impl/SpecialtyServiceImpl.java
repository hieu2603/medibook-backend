package com.sgu.clinic_service.service.impl;

import com.sgu.clinic_service.dto.request.specialty.SpecialtyCreateRequestDto;
import com.sgu.clinic_service.dto.request.specialty.SpecialtyUpdateRequestDto;
import com.sgu.clinic_service.dto.response.specialty.SpecialtyResponseDto;
import com.sgu.clinic_service.exception.ResourceNotFoundException;
import com.sgu.clinic_service.mapper.SpecialtyMapper;
import com.sgu.clinic_service.model.Specialty;
import com.sgu.clinic_service.repository.SpecialtyRepository;
import com.sgu.clinic_service.service.SpecialtyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SpecialtyServiceImpl implements SpecialtyService {
    private final SpecialtyRepository specialtyRepository;

    @Override
    public List<SpecialtyResponseDto> getAllSpecialties() {
        List<Specialty> specialties = specialtyRepository.findAll();

        return specialties.stream()
                .map(SpecialtyMapper::toDto)
                .toList();
    }

    @Override
    public SpecialtyResponseDto getSpecialtyById(UUID id) {
        Specialty specialty = specialtyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Specialty not found"));

        return SpecialtyMapper.toDto(specialty);
    }

    @Override
    public SpecialtyResponseDto createSpecialty(SpecialtyCreateRequestDto dto) {
        if (specialtyRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new IllegalArgumentException("Specialty name already exists");
        }

        Specialty specialty = SpecialtyMapper.toEntity(dto);

        Specialty createdSpecialty = specialtyRepository.save(specialty);

        return SpecialtyMapper.toDto(createdSpecialty);
    }

    @Override
    public SpecialtyResponseDto updateSpecialty(UUID id, SpecialtyUpdateRequestDto dto) {
        if (specialtyRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new IllegalArgumentException("Specialty name already exists");
        }

        Specialty specialty = specialtyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Specialty not found"));


        SpecialtyMapper.updateEntity(specialty, dto);

        Specialty updatedSpecialty = specialtyRepository.save(specialty);

        return SpecialtyMapper.toDto(updatedSpecialty);
    }
}
