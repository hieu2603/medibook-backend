package com.sgu.auth_service.dto.response.clinic;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class ClinicResponseDto {
    private UUID id;
    private String clinicName;
    private String phone;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String description;
    private BigDecimal price;
    private UUID userId;
}
