package com.sgu.auth_service.dto.response.login;

import com.sgu.auth_service.constant.Role;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class LoginResponseDto {
    private String token;
    private UUID id;
    private String email;
    private Role role;
    private String avatarUrl;
}
