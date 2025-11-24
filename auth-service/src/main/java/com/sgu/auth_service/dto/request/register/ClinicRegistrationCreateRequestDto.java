package com.sgu.auth_service.dto.request.register;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ClinicRegistrationCreateRequestDto {
    private String clinicName;
    private String email;
    private String phone;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String description;
    private BigDecimal price;
}
