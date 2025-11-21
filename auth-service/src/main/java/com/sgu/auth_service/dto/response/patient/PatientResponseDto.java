package com.sgu.auth_service.dto.response.patient;

import com.sgu.auth_service.constant.PatientGender;
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
