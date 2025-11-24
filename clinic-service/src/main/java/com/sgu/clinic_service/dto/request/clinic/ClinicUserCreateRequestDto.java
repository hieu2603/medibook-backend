package com.sgu.clinic_service.dto.request.clinic;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClinicUserCreateRequestDto {
    private String email;
}
