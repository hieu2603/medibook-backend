package com.sgu.patient_service.mapper;

import com.sgu.patient_service.dto.request.PatientCreateRequest;
import com.sgu.patient_service.dto.request.PatientUpdateRequest;
import com.sgu.patient_service.dto.response.PatientResponseDto;
import com.sgu.patient_service.enums.PatientGender;
import com.sgu.patient_service.model.Patient;

import java.time.LocalDate;

public class PatientMapper {

    // Từ Create DTO -> Entity
    public static Patient toEntity(PatientCreateRequest dto) {
        return Patient.builder()
                .fullName(dto.getFullName())
                .dob(dto.getDob())
                .gender(dto.getGender())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .userId(dto.getUserId())
                .build();
    }

    // Từ Entity -> Response DTO
    public static PatientResponseDto toDto(Patient patient) {
        return PatientResponseDto.builder()
                .id(patient.getId())
                .fullName(patient.getFullName())
                .dob(patient.getDob())
                .gender(patient.getGender())
                .phone(patient.getPhone())
                .address(patient.getAddress())
                .userId(patient.getUserId())
                .build();
    }

    // Update Entity từ Update DTO
    public static void updateEntity(Patient patient, PatientUpdateRequest dto) {
        String fullName = dto.getFullName();
        LocalDate dob = dto.getDob();
        PatientGender gender = dto.getGender();
        String phone = dto.getPhone();
        String address = dto.getAddress();

        if (fullName != null) {
            patient.setFullName(fullName);
        }
        if (dob != null) {
            patient.setDob(dob);
        }
        if (gender != null) {
            patient.setGender(gender);
        }
        if (phone != null) {
            patient.setPhone(phone);
        }
        if (address != null) {
            patient.setAddress(address);
        }
    }
}
