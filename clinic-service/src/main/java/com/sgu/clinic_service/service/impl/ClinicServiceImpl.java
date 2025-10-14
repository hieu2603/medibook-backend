package com.sgu.clinic_service.service.impl;

import com.sgu.clinic_service.dto.request.clinic.ClinicCreateRequestDto;
import com.sgu.clinic_service.dto.request.clinic.ClinicUpdateRequestDto;
import com.sgu.clinic_service.dto.response.clinic.ClinicResponseDto;
import com.sgu.clinic_service.exception.ResourceNotFoundException;
import com.sgu.clinic_service.mapper.ClinicMapper;
import com.sgu.clinic_service.model.Clinic;
import com.sgu.clinic_service.repository.ClinicRepository;
import com.sgu.clinic_service.service.ClinicService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ClinicServiceImpl implements ClinicService {
    private final ClinicRepository clinicRepository;

    @Override
    public List<ClinicResponseDto> getAllClinics() {
        List<Clinic> clinics = clinicRepository.findAll();

        return clinics.stream()
                .map(ClinicMapper::toDto)
                .toList();
    }

    @Override
    public ClinicResponseDto getClinicById(UUID id) {
        Clinic clinic = clinicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clinic not found"));

        return ClinicMapper.toDto(clinic);
    }

    @Override
    public ClinicResponseDto createClinic(ClinicCreateRequestDto requestDto) {
        Clinic clinic = Clinic.builder()
                .clinicName(requestDto.getClinicName())
                .phone(requestDto.getPhone())
                .address(requestDto.getAddress())
                .latitude(requestDto.getLatitude())
                .longitude(requestDto.getLongitude())
                .description(requestDto.getDescription())
                .price(requestDto.getPrice())
                .userId(requestDto.getUserId())
                .build();

        Clinic createdClinic = clinicRepository.save(clinic);

        return ClinicMapper.toDto(createdClinic);
    }

    @Override
    public ClinicResponseDto updateClinic(UUID id, ClinicUpdateRequestDto requestDto) {
        Clinic clinic = clinicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clinic not found with id: " + id));

        if (requestDto.getClinicName() != null) {
            clinic.setClinicName(requestDto.getClinicName());
        }
        if (requestDto.getPhone() != null) {
            clinic.setPhone(requestDto.getPhone());
        }
        if (requestDto.getAddress() != null) {
            clinic.setAddress(requestDto.getAddress());
        }
        if (requestDto.getLatitude() != null) {
            clinic.setLatitude(requestDto.getLatitude());
        }
        if (requestDto.getLongitude() != null) {
            clinic.setLongitude(requestDto.getLongitude());
        }
        if (requestDto.getDescription() != null) {
            clinic.setDescription(requestDto.getDescription());
        }
        if (requestDto.getPrice() != null) {
            clinic.setPrice(requestDto.getPrice());
        }

        Clinic updatedClinic = clinicRepository.save(clinic);

        return ClinicMapper.toDto(updatedClinic);
    }

    @Override
    public List<ClinicResponseDto> searchClinicsByName(String keyword) {
        List<Clinic> clinics = clinicRepository.findByClinicNameContainingIgnoreCase(keyword);

        return clinics.stream()
                .map(ClinicMapper::toDto)
                .toList();
    }
}
