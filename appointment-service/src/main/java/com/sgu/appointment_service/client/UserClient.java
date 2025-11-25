package com.sgu.appointment_service.client;

import com.sgu.appointment_service.dto.request.transfer.TransferRequestDto;
import com.sgu.appointment_service.dto.response.common.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service")
public interface UserClient {
    @PostMapping("/api/balance/appointment-payment")
    ApiResponse<Void> payForAppointment(@RequestBody TransferRequestDto dto);
}
