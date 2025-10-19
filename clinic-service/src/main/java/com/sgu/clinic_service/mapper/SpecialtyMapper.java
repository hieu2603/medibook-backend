package com.sgu.clinic_service.mapper;

import com.sgu.clinic_service.dto.request.specialty.SpecialtyCreateRequestDto;
import com.sgu.clinic_service.dto.request.specialty.SpecialtyUpdateRequestDto;
import com.sgu.clinic_service.dto.response.specialty.SpecialtyResponseDto;
import com.sgu.clinic_service.model.Specialty;

public class SpecialtyMapper {
    // Từ Create DTO -> Entity
    public static Specialty toEntity(SpecialtyCreateRequestDto dto) {
        return Specialty.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }

    // Từ Entity -> Response DTO
    public static SpecialtyResponseDto toDto(Specialty specialty) {
        return SpecialtyResponseDto.builder()
                .id(specialty.getId())
                .name(specialty.getName())
                .description(specialty.getDescription())
                .build();
    }

    // Update Entity từ Update DTO
    public static void updateEntity(Specialty specialty, SpecialtyUpdateRequestDto dto) {
        String name = dto.getName();
        String description = dto.getDescription();

        if (name != null) {
            specialty.setName(name);
        }
        if (description != null) {
            specialty.setDescription(description);
        }
    }
}
