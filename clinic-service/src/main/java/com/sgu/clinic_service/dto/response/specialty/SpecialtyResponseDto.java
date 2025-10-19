package com.sgu.clinic_service.dto.response.specialty;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class SpecialtyResponseDto {
    private UUID id;
    private String name;
    private String description;
}
