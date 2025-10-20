package com.sgu.patient_service.dto.response;

import java.time.LocalDate;
import java.util.UUID;

import com.sgu.patient_service.enums.PatientGender;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponseDto {
    private UUID patient_id;
    private String full_name;
    private LocalDate dob;
    private PatientGender gender;
    private String phone;
    private String address;
    private UUID user_id;
}