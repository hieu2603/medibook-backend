package com.sgu.auth_service.mapper;

import com.sgu.auth_service.constant.Role;
import com.sgu.auth_service.dto.request.register.RegisterRequestDto;
import com.sgu.auth_service.dto.response.login.LoginResponseDto;
import com.sgu.auth_service.dto.response.register.RegisterResponseDto;
import com.sgu.auth_service.model.User;

public class UserMapper {
    // Từ Register Dto -> Entity
    public static User toEntity(RegisterRequestDto dto, String encodedPassword) {
        return User.builder()
                .email(dto.getEmail())
                .password(encodedPassword)
                .role(Role.valueOf(dto.getRole().toUpperCase()))
                .build();
    }

    // Từ Entity -> Register Response DTO
    public static RegisterResponseDto toRegisterResponseDto(User user) {
        return RegisterResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .avatarUrl(user.getAvatarUrl())
                .balance(user.getBalance())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .build();
    }

    // Từ Login DTO -> Entity
    public static LoginResponseDto toLoginResponseDto(User user, String token) {
        return LoginResponseDto.builder()
                .token(token)
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }
}
