package com.sgu.clinic_service.dto.response.clinic;

import com.sgu.clinic_service.constant.RegistrationStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ClinicRegistrationResponseDto {
    private UUID id;
    private String email;
    private String clinicName;
    private String phone;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String description;
    private BigDecimal price;
    private RegistrationStatus registrationStatus;
    private LocalDateTime createdAt;
}
