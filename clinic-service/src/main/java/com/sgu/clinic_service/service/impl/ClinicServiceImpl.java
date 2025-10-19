package com.sgu.clinic_service.service.impl;

import com.sgu.clinic_service.dto.request.clinic.ClinicCreateRequestDto;
import com.sgu.clinic_service.dto.request.clinic.ClinicUpdateRequestDto;
import com.sgu.clinic_service.dto.response.clinic.ClinicResponseDto;
import com.sgu.clinic_service.dto.response.common.PaginationMeta;
import com.sgu.clinic_service.dto.response.common.PaginationResponse;
import com.sgu.clinic_service.exception.ResourceNotFoundException;
import com.sgu.clinic_service.mapper.ClinicMapper;
import com.sgu.clinic_service.model.Clinic;
import com.sgu.clinic_service.repository.ClinicRepository;
import com.sgu.clinic_service.service.ClinicService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ClinicServiceImpl implements ClinicService {
    private final ClinicRepository clinicRepository;

    @Override
    public PaginationResponse<ClinicResponseDto> getAllClinics(
            String name, int page, int size
    ) {
        int pageIndex = (page <= 0) ? 0 : page - 1;

        Pageable pageable = PageRequest.of(pageIndex, size, Sort.by("clinicName").ascending());

        Page<Clinic> clinicPage = (name == null || name.trim().isEmpty())
                ? clinicRepository.findAll(pageable)
                : clinicRepository.findByClinicNameContainingIgnoreCase(name, pageable);

        List<ClinicResponseDto> data = clinicPage.map(ClinicMapper::toDto).getContent();

        long totalItems = clinicPage.getTotalElements();

        PaginationMeta meta = PaginationMeta.builder()
                .currentPage(totalItems == 0 ? 0 : clinicPage.getNumber() + 1)
                .pageSize(clinicPage.getSize())
                .totalPages(clinicPage.getTotalPages())
                .totalItems(totalItems)
                .build();

        return PaginationResponse.<ClinicResponseDto>builder()
                .data(data)
                .meta(meta)
                .build();
    }

    @Override
    public ClinicResponseDto getClinicById(UUID id) {
        Clinic clinic = clinicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clinic not found"));

        return ClinicMapper.toDto(clinic);
    }

    @Override
    public ClinicResponseDto createClinic(ClinicCreateRequestDto dto) {
        Clinic clinic = ClinicMapper.toEntity(dto);

        Clinic createdClinic = clinicRepository.save(clinic);

        return ClinicMapper.toDto(createdClinic);
    }

    @Override
    public ClinicResponseDto updateClinic(UUID id, ClinicUpdateRequestDto dto) {
        Clinic clinic = clinicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clinic not found"));

        ClinicMapper.updateEntity(clinic, dto);

        Clinic updatedClinic = clinicRepository.save(clinic);

        return ClinicMapper.toDto(updatedClinic);
    }
}
