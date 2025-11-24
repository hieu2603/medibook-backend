package com.sgu.user_service.controller;

import com.sgu.user_service.dto.common.ApiResponse;
import com.sgu.user_service.dto.request.TransferRequestDto;
import com.sgu.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/balance")
@RequiredArgsConstructor
public class TransferController {

    private final UserService userService;

    // Appointment Service sẽ gọi endpoint này (frontend không cần xử lý endpoint này)
    @PostMapping("/appointment-payment")
    public ResponseEntity<ApiResponse<Void>> payForAppointment(
            @Valid @RequestBody TransferRequestDto dto
    ) {
        userService.transfer(dto);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Appointment payment successful")
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    // Appointment Service sẽ gọi endpoint này (frontend không cần xử lý endpoint này)
    @PostMapping("/refund")
    public ResponseEntity<ApiResponse<Void>> refundPayment(
            @Valid @RequestBody TransferRequestDto dto
    ) {
        userService.transfer(dto);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Refund successful")
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
