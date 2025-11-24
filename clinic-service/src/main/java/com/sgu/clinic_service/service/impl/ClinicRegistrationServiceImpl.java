package com.sgu.clinic_service.service.impl;

import com.sgu.clinic_service.client.AuthClient;
import com.sgu.clinic_service.constant.RegistrationStatus;
import com.sgu.clinic_service.dto.common.ApiResponse;
import com.sgu.clinic_service.dto.common.PaginationMeta;
import com.sgu.clinic_service.dto.common.PaginationResponse;
import com.sgu.clinic_service.dto.request.clinic.ClinicCreateRequestDto;
import com.sgu.clinic_service.dto.request.clinic.ClinicRegistrationCreateRequestDto;
import com.sgu.clinic_service.dto.response.clinic.ClinicRegistrationResponseDto;
import com.sgu.clinic_service.dto.response.clinic.RegisterClinicResponseDto;
import com.sgu.clinic_service.exception.ResourceNotFoundException;
import com.sgu.clinic_service.mapper.ClinicRegistrationMapper;
import com.sgu.clinic_service.model.ClinicRegistration;
import com.sgu.clinic_service.repository.ClinicRegistrationRepository;
import com.sgu.clinic_service.service.ClinicRegistrationService;
import com.sgu.clinic_service.service.ClinicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClinicRegistrationServiceImpl implements ClinicRegistrationService {

    private final ClinicRegistrationRepository clinicRegistrationRepository;
    private final AuthClient authClient;
    private final ClinicService clinicService;

    @Override
    public ClinicRegistrationResponseDto createClinicRegistration(ClinicRegistrationCreateRequestDto dto) {
        ClinicRegistration clinicRegistration = ClinicRegistrationMapper.toEntity(dto);

        ClinicRegistration newClinicRegistration = clinicRegistrationRepository.save(clinicRegistration);

        return ClinicRegistrationMapper.toDto(newClinicRegistration);
    }

    @Override
    @Transactional
    public ClinicRegistrationResponseDto approveRegistration(UUID registrationId) {
        ClinicRegistration clinicRegistration = clinicRegistrationRepository
                .findById(registrationId)
                .orElseThrow(() -> new ResourceNotFoundException("Clinic Registration not found"));

        if (!clinicRegistration.getRegistrationStatus().equals(RegistrationStatus.PENDING)) {
            throw new IllegalArgumentException("Only PENDING registration can be approved");
        }

        // Gọi qua auth-service để tạo user
        ApiResponse<RegisterClinicResponseDto> response = authClient.registerClinic(clinicRegistration.getEmail());
        RegisterClinicResponseDto userOfClinic = response.getData();

        // Tạo clinic mới
        ClinicCreateRequestDto clinic = ClinicCreateRequestDto.builder()
                .clinicName(clinicRegistration.getClinicName())
                .phone(clinicRegistration.getPhone())
                .address(clinicRegistration.getAddress())
                .latitude(clinicRegistration.getLatitude())
                .longitude(clinicRegistration.getLongitude())
                .description(clinicRegistration.getDescription())
                .price(clinicRegistration.getPrice())
                .userId(userOfClinic.getId())
                .build();

        clinicService.createClinic(clinic);

        clinicRegistration.setRegistrationStatus(RegistrationStatus.APPROVED);

        clinicRegistrationRepository.save(clinicRegistration);

        return ClinicRegistrationMapper.toDto(clinicRegistration);
    }

    @Override
    public ClinicRegistrationResponseDto rejectRegistration(UUID registrationId) {
        ClinicRegistration clinicRegistration = clinicRegistrationRepository
                .findById(registrationId)
                .orElseThrow(() -> new ResourceNotFoundException("Clinic Registration not found"));

        if (!clinicRegistration.getRegistrationStatus().equals(RegistrationStatus.PENDING)) {
            throw new IllegalArgumentException("Only PENDING registration can be rejected");
        }

        clinicRegistration.setRegistrationStatus(RegistrationStatus.REJECTED);

        clinicRegistrationRepository.save(clinicRegistration);

        return ClinicRegistrationMapper.toDto(clinicRegistration);
    }

    @Override
    public PaginationResponse<ClinicRegistrationResponseDto> getRegistrations(int page, int size) {
        int pageIndex = (page <= 0) ? 0 : page - 1;

        Pageable pageable = PageRequest.of(pageIndex, size, Sort.by("createdAt").descending());

        Page<ClinicRegistration> clinicRegistrationPage = clinicRegistrationRepository
                .findAll(pageable);

        List<ClinicRegistrationResponseDto> data = clinicRegistrationPage
                .map(ClinicRegistrationMapper::toDto)
                .getContent();

        long totalItems = clinicRegistrationPage.getTotalElements();

        PaginationMeta meta = PaginationMeta.builder()
                .currentPage(totalItems == 0 ? 0 : clinicRegistrationPage.getNumber() + 1)
                .pageSize(clinicRegistrationPage.getSize())
                .totalPages(clinicRegistrationPage.getTotalPages())
                .totalItems(totalItems)
                .build();

        return PaginationResponse.<ClinicRegistrationResponseDto>builder()
                .meta(meta)
                .data(data)
                .build();
    }
}
