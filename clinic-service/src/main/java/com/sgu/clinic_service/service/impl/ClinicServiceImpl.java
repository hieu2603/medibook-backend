package com.sgu.clinic_service.service.impl;

import com.sgu.clinic_service.dto.request.clinic.ClinicCreateRequestDto;
import com.sgu.clinic_service.dto.request.clinic.ClinicUpdateRequestDto;
import com.sgu.clinic_service.dto.response.clinic.ClinicResponseDto;
import com.sgu.clinic_service.mapper.ClinicMapper;
import com.sgu.clinic_service.model.Clinic;
import com.sgu.clinic_service.repository.ClinicRepository;
import com.sgu.clinic_service.security.ClinicPermissionValidator;
import com.sgu.clinic_service.service.ClinicService;
import com.sgu.common.dto.PaginationMeta;
import com.sgu.common.dto.PaginationResponse;
import com.sgu.common.exception.ResourceNotFoundException;
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
    private final ClinicPermissionValidator permissionValidator;

    @Override
    public PaginationResponse<ClinicResponseDto> getClinics(
            String name, Double latitude, Double longitude, Double radius,
            int page, int size
    ) {
        int pageIndex = (page <= 0) ? 0 : page - 1;

        Pageable pageable;
        Page<Clinic> clinicPage;

        boolean hasNearby = latitude != null && longitude != null && radius != null;
        boolean hasName = name != null && !name.trim().isEmpty();

        if (hasNearby) {
            pageable = PageRequest.of(pageIndex, size);
            clinicPage = clinicRepository.findByNameAndNearby(
                    hasName ? name : null,
                    latitude, longitude, radius,
                    pageable
            );
        } else if (hasName) {
            pageable = PageRequest.of(pageIndex, size, Sort.by("clinicName").ascending());
            clinicPage = clinicRepository.findByClinicNameContainingIgnoreCase(name, pageable);
        } else {
            pageable = PageRequest.of(pageIndex, size, Sort.by("clinicName").ascending());
            clinicPage = clinicRepository.findAll(pageable);
        }

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
    public ClinicResponseDto updateClinic(UUID id, ClinicUpdateRequestDto dto, UUID userId, String role) {
        Clinic clinic = clinicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clinic not found"));

        permissionValidator.validateUpdatePermission(clinic, userId, role);

        ClinicMapper.updateEntity(clinic, dto);

        Clinic updatedClinic = clinicRepository.save(clinic);

        return ClinicMapper.toDto(updatedClinic);
    }
}
