package com.sgu.clinic_service.mapper;

import com.sgu.clinic_service.dto.request.clinic.ClinicCreateRequestDto;
import com.sgu.clinic_service.dto.request.clinic.ClinicUpdateRequestDto;
import com.sgu.clinic_service.dto.response.clinic.ClinicResponseDto;
import com.sgu.clinic_service.model.Clinic;

import java.math.BigDecimal;

public class ClinicMapper {
    // Từ Create DTO -> Entity
    public static Clinic toEntity(ClinicCreateRequestDto dto) {
        return Clinic.builder()
                .clinicName(dto.getClinicName())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .userId(dto.getUserId())
                .build();
    }

    // Từ Entity -> Response DTO
    public static ClinicResponseDto toDto(Clinic clinic) {
        return ClinicResponseDto.builder()
                .id(clinic.getId())
                .clinicName(clinic.getClinicName())
                .phone(clinic.getPhone())
                .address(clinic.getAddress())
                .latitude(clinic.getLatitude())
                .longitude(clinic.getLongitude())
                .description(clinic.getDescription())
                .price(clinic.getPrice())
                .build();
    }

    // Update Entity từ Update DTO
    public static void updateEntity(Clinic clinic, ClinicUpdateRequestDto dto) {
        String clinicName = dto.getClinicName();
        String phone = dto.getPhone();
        String address = dto.getAddress();
        BigDecimal latitude = dto.getLatitude();
        BigDecimal longitude = dto.getLongitude();
        String description = dto.getDescription();
        BigDecimal price = dto.getPrice();

        if (clinicName != null) {
            clinic.setClinicName(clinicName);
        }
        if (phone != null) {
            clinic.setPhone(phone);
        }
        if (address != null) {
            clinic.setAddress(address);
        }
        if (latitude != null) {
            clinic.setLatitude(latitude);
        }
        if (longitude != null) {
            clinic.setLongitude(longitude);
        }
        if (description != null) {
            clinic.setDescription(description);
        }
        if (price != null) {
            clinic.setPrice(price);
        }
    }
}
