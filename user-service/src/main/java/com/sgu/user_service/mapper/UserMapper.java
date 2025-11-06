package com.sgu.user_service.mapper;

import com.sgu.user_service.dto.response.UserResponseDto;
import com.sgu.user_service.model.User;

public class UserMapper {
    public static UserResponseDto toDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .avatarUrl(user.getAvatarUrl())
                .balance(user.getBalance())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
