package com.sgu.auth_service.service;

import com.sgu.auth_service.dto.request.login.LoginRequestDto;
import com.sgu.auth_service.dto.request.password.ChangePasswordRequestDto;
import com.sgu.auth_service.dto.request.password.ForgotPasswordRequestDto;
import com.sgu.auth_service.dto.request.register.RegisterRequestDto;
import com.sgu.auth_service.dto.response.login.LoginResponseDto;
import com.sgu.auth_service.dto.response.register.RegisterResponseDto;

public interface AuthService {
    RegisterResponseDto register(RegisterRequestDto dto);

    LoginResponseDto login(LoginRequestDto dto);

    void forgotPassword(ForgotPasswordRequestDto dto);

    void changePassword(ChangePasswordRequestDto dto, String email);
}
