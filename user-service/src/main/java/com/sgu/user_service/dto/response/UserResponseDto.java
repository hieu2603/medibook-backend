package com.sgu.user_service.dto.response;

import com.sgu.user_service.constant.Role;
import com.sgu.user_service.constant.Status;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class UserResponseDto {
    private UUID id;
    private String email;
    private Role role;
    private String avatarUrl;
    private BigDecimal balance;
    private Status status;
    private LocalDate createdAt;
}
