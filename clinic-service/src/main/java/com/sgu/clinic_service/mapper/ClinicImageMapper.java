package com.sgu.clinic_service.mapper;

import com.sgu.clinic_service.dto.response.clinic.ClinicImageResponseDto;
import com.sgu.clinic_service.model.ClinicImage;

public class ClinicImageMapper {

    // Từ Entity -> Response DTO
    public static ClinicImageResponseDto toDto(ClinicImage clinicImage) {
        return ClinicImageResponseDto.builder()
                .imgId(clinicImage.getImgId())
                .url(clinicImage.getUrl())
                .clinicId(clinicImage.getClinicId())
                .build();
    }
}
