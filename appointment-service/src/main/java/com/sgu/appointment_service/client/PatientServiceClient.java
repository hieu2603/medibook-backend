package com.sgu.appointment_service.client;

import com.sgu.appointment_service.dto.response.PatientResponseDto;
import com.sgu.appointment_service.dto.response.common.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
    name = "patient-service",
    url = "${patient-service.url}"
)
public interface PatientServiceClient {
    
    @GetMapping("/api/patients/{id}")
    ResponseEntity<ApiResponse<PatientResponseDto>> getPatientById(
            @PathVariable("id") UUID patientId
    );
}

