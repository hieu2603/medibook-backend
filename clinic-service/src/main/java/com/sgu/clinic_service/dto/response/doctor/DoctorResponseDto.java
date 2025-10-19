package com.sgu.clinic_service.dto.response.doctor;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class DoctorResponseDto {
    private UUID id;
    private UUID clinicId;
    private String fullName;
    private UUID specialtyId;
    private String phoneNumber;
    private String description;
    private String status;
}
