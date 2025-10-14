package com.sgu.clinic_service.mapper;

import com.sgu.clinic_service.dto.response.clinic.ClinicResponseDto;
import com.sgu.clinic_service.model.Clinic;

public class ClinicMapper {
    public static ClinicResponseDto toDto(Clinic clinic) {
        return ClinicResponseDto.builder()
                .clinic_id(clinic.getId())
                .clinic_name(clinic.getClinicName())
                .phone(clinic.getPhone())
                .address(clinic.getAddress())
                .latitude(clinic.getLatitude())
                .longitude(clinic.getLongitude())
                .description(clinic.getDescription())
                .price(clinic.getPrice())
                .build();
    }
}
