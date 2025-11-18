package com.sgu.clinic_service.dto.response.clinic;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ClinicImageResponseDto {
    private UUID imgId;
    private String url;
    private UUID clinicId;
}
