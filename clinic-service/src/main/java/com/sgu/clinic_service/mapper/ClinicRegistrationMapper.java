package com.sgu.clinic_service.mapper;

import com.sgu.clinic_service.dto.request.clinic.ClinicRegistrationCreateRequestDto;
import com.sgu.clinic_service.dto.response.clinic.ClinicRegistrationResponseDto;
import com.sgu.clinic_service.model.ClinicRegistration;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClinicRegistrationMapper {

    // Từ Create DTO -> Entity
    public static ClinicRegistration toEntity(ClinicRegistrationCreateRequestDto dto) {
        return ClinicRegistration.builder()
                .email(dto.getEmail())
                .clinicName(dto.getClinicName())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .build();
    }

    // Từ Entity -> Response DTO
    public static ClinicRegistrationResponseDto toDto(ClinicRegistration clinicRegistration) {
        return ClinicRegistrationResponseDto.builder()
                .id(clinicRegistration.getId())
                .email(clinicRegistration.getEmail())
                .clinicName(clinicRegistration.getClinicName())
                .phone(clinicRegistration.getPhone())
                .address(clinicRegistration.getAddress())
                .latitude(clinicRegistration.getLatitude())
                .longitude(clinicRegistration.getLongitude())
                .description(clinicRegistration.getDescription())
                .price(clinicRegistration.getPrice())
                .registrationStatus(clinicRegistration.getRegistrationStatus())
                .createdAt(clinicRegistration.getCreatedAt())
                .build();
    }
}
