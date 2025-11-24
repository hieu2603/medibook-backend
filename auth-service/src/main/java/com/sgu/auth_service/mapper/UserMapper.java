package com.sgu.auth_service.mapper;

import com.sgu.auth_service.constant.Role;
import com.sgu.auth_service.dto.request.register.RegisterPatientRequestDto;
import com.sgu.auth_service.dto.response.login.LoginResponseDto;
import com.sgu.auth_service.dto.response.patient.PatientResponseDto;
import com.sgu.auth_service.dto.response.register.RegisterClinicResponseDto;
import com.sgu.auth_service.dto.response.register.RegisterPatientResponseDto;
import com.sgu.auth_service.model.User;

public class UserMapper {
    // Từ Register Patient Dto -> Entity
    public static User fromRegisterPatientToEntity(RegisterPatientRequestDto dto, String encodedPassword) {
        return User.builder()
                .email(dto.getEmail())
                .password(encodedPassword)
                .role(Role.valueOf(dto.getRole().toUpperCase()))
                .build();
    }

    // Từ Register Clinic Dto -> Entity
    public static User fromRegisterClinicToEntity(String email, String encodedPassword) {
        return User.builder()
                .email(email)
                .password(encodedPassword)
                .role(Role.CLINIC)
                .build();
    }

    // Từ Entity -> Register Patient Response DTO
    public static RegisterPatientResponseDto toRegisterPatientResponseDto(User user, PatientResponseDto patient) {
        return RegisterPatientResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .avatarUrl(user.getAvatarUrl())
                .balance(user.getBalance())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .patient(patient)
                .build();
    }

    // Từ Entity -> Register Clinic Response DTO
    public static RegisterClinicResponseDto toRegisterClinicResponseDto(User user) {
        return RegisterClinicResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .avatarUrl(user.getAvatarUrl())
                .balance(user.getBalance())
                .pendingBalance(user.getPendingBalance())
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
