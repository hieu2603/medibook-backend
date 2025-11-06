package com.sgu.user_service.controller;

import com.sgu.common.dto.ApiResponse;
import com.sgu.common.dto.PaginationResponse;
import com.sgu.user_service.constant.PaymentType;
import com.sgu.user_service.dto.request.PaymentRequestDto;
import com.sgu.user_service.dto.response.UserResponseDto;
import com.sgu.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader("X-User-Role") String role
    ) {
        PaginationResponse<UserResponseDto> result = userService.getUsers(
                page, size, role
        );

        ApiResponse<List<UserResponseDto>> response = ApiResponse.<List<UserResponseDto>>builder()
                .status(HttpStatus.OK.value())
                .message("Users retrieved successfully")
                .data(result.getData())
                .meta(result.getMeta())
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserById(
            @PathVariable UUID id,
            @RequestHeader("X-User-Role") String role
    ) {
        UserResponseDto user = userService.getUserById(id, role);

        ApiResponse<UserResponseDto> response = ApiResponse.<UserResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message("User retrieved successfully")
                .data(user)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PatchMapping("/{id}/avatar")
    public ResponseEntity<ApiResponse<String>> updateAvatar(
            @PathVariable(name = "id") UUID targetId,
            @RequestParam("file") MultipartFile file,
            @RequestHeader("X-User-Id") String userId
    ) throws IOException {
        String imageUrl = userService.updateAvatar(
                targetId,
                UUID.fromString(userId),
                file
        );

        ApiResponse<String> response = ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .message("Avatar updated successfully")
                .data(imageUrl)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<ApiResponse<Void>> deposit(
            @PathVariable UUID id,
            @Valid @RequestBody PaymentRequestDto dto
    ) {
        dto.setUserId(id);
        dto.setType(PaymentType.DEPOSIT);
        userService.updateBalance(dto);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Deposit successful")
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<ApiResponse<Void>> withdraw(
            @PathVariable UUID id,
            @Valid @RequestBody PaymentRequestDto dto
    ) {
        dto.setUserId(id);
        dto.setType(PaymentType.WITHDRAW);
        userService.updateBalance(dto);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Withdraw successful")
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
