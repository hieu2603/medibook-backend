package com.sgu.clinic_service.client;

import com.sgu.clinic_service.dto.common.ApiResponse;
import com.sgu.clinic_service.dto.response.clinic.RegisterClinicResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "auth-service")
public interface AuthClient {

    @PostMapping("/api/auth/register-clinic")
    ApiResponse<RegisterClinicResponseDto> registerClinic(
            @RequestBody String email
    );
}
