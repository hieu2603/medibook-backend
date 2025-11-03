package com.sgu.auth_service.controller;

import com.sgu.auth_service.dto.request.login.LoginRequestDto;
import com.sgu.auth_service.dto.request.password.ChangePasswordRequestDto;
import com.sgu.auth_service.dto.request.password.ForgotPasswordRequestDto;
import com.sgu.auth_service.dto.request.register.RegisterRequestDto;
import com.sgu.auth_service.dto.response.common.ApiResponse;
import com.sgu.auth_service.dto.response.login.LoginResponseDto;
import com.sgu.auth_service.dto.response.register.RegisterResponseDto;
import com.sgu.auth_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponseDto>> register(
            @Valid @RequestBody RegisterRequestDto dto
    ) {
        RegisterResponseDto registeredUser = authService.register(dto);

        ApiResponse<RegisterResponseDto> response = ApiResponse.<RegisterResponseDto>builder()
                .status(HttpStatus.CREATED.value())
                .message("User registered successfully")
                .data(registeredUser)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(
            @Valid @RequestBody LoginRequestDto dto
    ) {
        LoginResponseDto loggedInUser = authService.login(dto);

        ApiResponse<LoginResponseDto> response = ApiResponse.<LoginResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message("User logged in successfully")
                .data(loggedInUser)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequestDto dto
    ) {
        authService.forgotPassword(dto);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("A new password has been sent to your email address")
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @Valid @RequestBody ChangePasswordRequestDto dto,
            @RequestHeader("X-User-Email") String email
    ) {
        authService.changePassword(dto, email);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Password changed successfully")
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
