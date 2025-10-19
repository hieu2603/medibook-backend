package com.sgu.clinic_service.mapper;

import com.sgu.clinic_service.dto.request.doctor.DoctorCreateRequestDto;
import com.sgu.clinic_service.dto.request.doctor.DoctorUpdateRequestDto;
import com.sgu.clinic_service.dto.response.doctor.DoctorResponseDto;
import com.sgu.clinic_service.model.Doctor;

import java.util.UUID;

public class DoctorMapper {
    // Từ Create DTO -> Entity
    public static Doctor toEntity(DoctorCreateRequestDto dto) {
        return Doctor.builder()
                .clinicId(dto.getClinicId())
                .fullName(dto.getFullName())
                .specialtyId(dto.getSpecialtyId())
                .phoneNumber(dto.getPhoneNumber())
                .description(dto.getDescription())
                .build();
    }

    // Từ Entity -> Response DTO
    public static DoctorResponseDto toDto(Doctor doctor) {
        return DoctorResponseDto.builder()
                .id(doctor.getId())
                .clinicId(doctor.getClinicId())
                .fullName(doctor.getFullName())
                .specialtyId(doctor.getSpecialtyId())
                .phoneNumber(doctor.getPhoneNumber())
                .description(doctor.getDescription())
                .status(doctor.getStatus().name())
                .build();
    }

    // Update Entity từ Update DTO
    public static void updateEntity(Doctor doctor, DoctorUpdateRequestDto dto) {
        String fullName = dto.getFullName();
        UUID specialtyId = dto.getSpecialtyId();
        String phoneNumber = dto.getPhoneNumber();
        String description = dto.getDescription();

        if (fullName != null) {
            doctor.setFullName(fullName);
        }
        if (specialtyId != null) {
            doctor.setSpecialtyId(specialtyId);
        }
        if (phoneNumber != null) {
            doctor.setPhoneNumber(phoneNumber);
        }
        if (description != null) {
            doctor.setDescription(description);
        }
    }
}
