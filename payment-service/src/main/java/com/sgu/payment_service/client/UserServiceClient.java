package com.sgu.payment_service.client;

import com.sgu.payment_service.dto.request.PaymentRequestDto;
import com.sgu.payment_service.dto.response.payment.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(
    name = "user-service",
    url = "${user-service.url}"  // http://localhost:8081
)
public interface UserServiceClient {
    
    // Gọi endpoint deposit của User Service
    @PostMapping("/api/users/{id}/deposit")
    ResponseEntity<ApiResponse<Void>> deposit(
            @PathVariable("id") UUID userId,
            @RequestBody PaymentRequestDto request
    );
    
    // Gọi endpoint withdraw của User Service
    @PostMapping("/api/users/{id}/withdraw")
    ResponseEntity<ApiResponse<Void>> withdraw(
            @PathVariable("id") UUID userId,
            @RequestBody PaymentRequestDto request
    );
}