package com.sgu.appointment_service.client;

import com.sgu.appointment_service.constant.UserServiceEndpoints;
import com.sgu.appointment_service.dto.request.TransferRequestDto;
import com.sgu.appointment_service.dto.response.common.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    name = "user-service",
    url = "${user-service.url}"
)
public interface UserServiceClient {
    
    @PostMapping(UserServiceEndpoints.APPOINTMENT_PAYMENT)
    ResponseEntity<ApiResponse<Void>> payForAppointment(
            @RequestBody TransferRequestDto dto
    );
    
    @PostMapping(UserServiceEndpoints.REFUND)
    ResponseEntity<ApiResponse<Void>> refundPayment(
            @RequestBody TransferRequestDto dto
    );
}

