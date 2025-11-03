package com.sgu.auth_service.dto.response.register;

import com.sgu.auth_service.constant.Role;
import com.sgu.auth_service.constant.Status;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class RegisterResponseDto {
    private UUID id;
    private String email;
    private Role role;
    private String avatarUrl;
    private BigDecimal balance;
    private Status status;
    private LocalDate createdAt;
}
