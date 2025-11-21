package com.sgu.auth_service.client;

import com.sgu.auth_service.dto.common.ApiResponse;
import com.sgu.auth_service.dto.request.patient.PatientCreateRequest;
import com.sgu.auth_service.dto.response.patient.PatientResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "patient-service")
public interface PatientClient {

    @PostMapping("/api/patients")
    ApiResponse<PatientResponseDto> createPatient(@RequestBody PatientCreateRequest request);
}
