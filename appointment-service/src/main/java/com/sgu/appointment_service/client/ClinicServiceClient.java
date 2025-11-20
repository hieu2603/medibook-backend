package com.sgu.appointment_service.client;

import com.sgu.appointment_service.dto.response.ClinicResponseDto;
import com.sgu.appointment_service.dto.response.common.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
    name = "clinic-service",
    url = "${clinic-service.url}"
)
public interface ClinicServiceClient {
    
    @GetMapping("/api/clinics/{id}")
    ResponseEntity<ApiResponse<ClinicResponseDto>> getClinicById(
            @PathVariable("id") UUID clinicId
    );
}

