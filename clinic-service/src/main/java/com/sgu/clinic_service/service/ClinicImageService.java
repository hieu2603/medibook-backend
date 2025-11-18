package com.sgu.clinic_service.service;

import com.sgu.clinic_service.dto.response.clinic.ClinicImageResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ClinicImageService {
    List<String> uploadImages(
            UUID clinicId,
            List<MultipartFile> files
    ) throws IOException;

    List<ClinicImageResponseDto> getImagesByClinicId(UUID clinicId);

    void deleteImage(UUID imageId) throws IOException;
}
