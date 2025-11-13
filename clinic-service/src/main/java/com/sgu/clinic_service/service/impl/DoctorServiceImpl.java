package com.sgu.clinic_service.service.impl;

import com.sgu.clinic_service.constant.DoctorStatus;
import com.sgu.clinic_service.dto.common.PaginationMeta;
import com.sgu.clinic_service.dto.common.PaginationResponse;
import com.sgu.clinic_service.dto.request.doctor.DoctorCreateRequestDto;
import com.sgu.clinic_service.dto.request.doctor.DoctorUpdateRequestDto;
import com.sgu.clinic_service.dto.response.doctor.DoctorResponseDto;
import com.sgu.clinic_service.exception.ResourceNotFoundException;
import com.sgu.clinic_service.mapper.DoctorMapper;
import com.sgu.clinic_service.model.Doctor;
import com.sgu.clinic_service.repository.DoctorRepository;
import com.sgu.clinic_service.security.DoctorPermissionValidator;
import com.sgu.clinic_service.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final DoctorPermissionValidator permissionValidator;

    @Override
    public PaginationResponse<DoctorResponseDto> getAllDoctorsByClinicId(
            UUID clinicId, int page, int size
    ) {
        int pageIndex = (page <= 0) ? 0 : page - 1;

        Pageable pageable = PageRequest.of(pageIndex, size);

        Page<Doctor> doctorPage = doctorRepository.findAllByClinicId(clinicId, pageable);

        List<DoctorResponseDto> doctors = doctorPage.map(DoctorMapper::toDto).getContent();

        long totalItems = doctorPage.getTotalElements();

        PaginationMeta meta = PaginationMeta.builder()
                .currentPage(totalItems == 0 ? 0 : doctorPage.getNumber() + 1)
                .pageSize(doctorPage.getSize())
                .totalPages(doctorPage.getTotalPages())
                .totalItems(totalItems)
                .build();

        return PaginationResponse.<DoctorResponseDto>builder()
                .data(doctors)
                .meta(meta)
                .build();
    }

    @Override
    public PaginationResponse<DoctorResponseDto> getAllDoctorsByClinicIdAndStatus(UUID clinicId, DoctorStatus status, int page, int size) {
        int pageIndex = (page <= 0) ? 0 : page - 1;

        Pageable pageable = PageRequest.of(pageIndex, size);

        Page<Doctor> doctorPage = doctorRepository.findAllByClinicIdAndStatus(clinicId, status, pageable);

        List<DoctorResponseDto> doctors = doctorPage.map(DoctorMapper::toDto).getContent();

        long totalItems = doctorPage.getTotalElements();

        PaginationMeta meta = PaginationMeta.builder()
                .currentPage(totalItems == 0 ? 0 : doctorPage.getNumber() + 1)
                .pageSize(doctorPage.getSize())
                .totalPages(doctorPage.getTotalPages())
                .totalItems(totalItems)
                .build();

        return PaginationResponse.<DoctorResponseDto>builder()
                .data(doctors)
                .meta(meta)
                .build();
    }

    @Override
    public DoctorResponseDto getDoctorById(UUID id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        return DoctorMapper.toDto(doctor);
    }

    @Override
    public DoctorResponseDto createDoctor(DoctorCreateRequestDto dto, String role) {
        permissionValidator.validateCreatePermission(role);

        Doctor doctor = DoctorMapper.toEntity(dto);

        Doctor createdDoctor = doctorRepository.save(doctor);

        return DoctorMapper.toDto(createdDoctor);
    }

    @Override
    public DoctorResponseDto updateDoctor(UUID id, DoctorUpdateRequestDto dto, UUID userId, String role) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        permissionValidator.validateUpdatePermission(doctor, userId, role);

        DoctorMapper.updateEntity(doctor, dto);

        Doctor updatedDoctor = doctorRepository.save(doctor);

        return DoctorMapper.toDto(updatedDoctor);
    }

    @Override
    public DoctorResponseDto lockDoctor(UUID id, UUID userId, String role) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        permissionValidator.validateLockPermission(doctor, userId, role);

        doctor.setStatus(DoctorStatus.INACTIVE);

        doctorRepository.save(doctor);

        return DoctorMapper.toDto(doctor);
    }

    @Override
    public DoctorResponseDto unlockDoctor(UUID id, UUID userId, String role) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        permissionValidator.validateUnlockPermission(doctor, userId, role);

        doctor.setStatus(DoctorStatus.ACTIVE);

        doctorRepository.save(doctor);

        return DoctorMapper.toDto(doctor);
    }
}
