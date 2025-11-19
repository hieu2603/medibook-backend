package com.sgu.patient_service.dto.response;

import com.sgu.patient_service.enums.PatientGender;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class PatientResponseDto {
    private UUID id;
    private String fullName;
    private LocalDate dob;
    private PatientGender gender;
    private String phone;
    private String address;
    private UUID userId;
}