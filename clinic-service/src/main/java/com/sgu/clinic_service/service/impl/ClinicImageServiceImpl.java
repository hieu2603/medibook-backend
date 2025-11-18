package com.sgu.clinic_service.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.sgu.clinic_service.dto.response.clinic.ClinicImageResponseDto;
import com.sgu.clinic_service.exception.ResourceNotFoundException;
import com.sgu.clinic_service.mapper.ClinicImageMapper;
import com.sgu.clinic_service.model.ClinicImage;
import com.sgu.clinic_service.repository.ClinicImageRepository;
import com.sgu.clinic_service.security.ClinicPermissionValidator;
import com.sgu.clinic_service.service.ClinicImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClinicImageServiceImpl implements ClinicImageService {

    private final ClinicImageRepository clinicImageRepository;
    private final Cloudinary cloudinary;
    private final ClinicPermissionValidator clinicPermissionValidator;

    @Override
    public List<String> uploadImages(UUID clinicId, List<MultipartFile> files) throws IOException {
        // Kiểm tra số lượng hiện tại
        List<ClinicImage> existing = clinicImageRepository.findByClinicId(clinicId);
        if (existing.size() + files.size() > 8) {
            throw new IllegalArgumentException("Clinics can only upload a maximum of 8 images each");
        }

        List<String> urls = new ArrayList<>();

        for (MultipartFile file : files) {
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("folder", "clinic/" + clinicId)
            );

            String url = (String) uploadResult.get("secure_url");
            ClinicImage img = new ClinicImage();
            img.setUrl(url);
            img.setClinicId(clinicId);
            clinicImageRepository.save(img);

            urls.add(url);
        }
        return urls;
    }

    @Override
    public List<ClinicImageResponseDto> getImagesByClinicId(UUID clinicId) {
        List<ClinicImage> images = clinicImageRepository.findByClinicId(clinicId);

        return images.stream()
                .map(ClinicImageMapper::toDto)
                .toList();
    }

    @Override
    public void deleteImage(UUID imageId) throws IOException {
        ClinicImage image = clinicImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found"));

        // Xóa ảnh trên cloudinary
        String publicId = extractPublicIdFromUrl(image.getUrl());
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

        clinicImageRepository.delete(image);
    }

    private String extractPublicIdFromUrl(String url) {
        int dotIndex = url.lastIndexOf(".");
        return url.substring(url.indexOf("clinic/"), dotIndex);
    }
}
