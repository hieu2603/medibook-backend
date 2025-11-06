package com.sgu.auth_service.service.impl;

import com.sgu.auth_service.constant.Status;
import com.sgu.auth_service.dto.request.login.LoginRequestDto;
import com.sgu.auth_service.dto.request.password.ChangePasswordRequestDto;
import com.sgu.auth_service.dto.request.password.ForgotPasswordRequestDto;
import com.sgu.auth_service.dto.request.register.RegisterRequestDto;
import com.sgu.auth_service.dto.response.login.LoginResponseDto;
import com.sgu.auth_service.dto.response.register.RegisterResponseDto;
import com.sgu.auth_service.event.EmailEventProducer;
import com.sgu.auth_service.mapper.UserMapper;
import com.sgu.auth_service.model.User;
import com.sgu.auth_service.repository.UserRepository;
import com.sgu.auth_service.security.AuthPermissionValidator;
import com.sgu.auth_service.service.AuthService;
import com.sgu.auth_service.utils.JwtUtil;
import com.sgu.auth_service.utils.PasswordUtil;
import com.sgu.common.exception.EmailAlreadyExistsException;
import com.sgu.common.exception.InvalidCredentialsException;
import com.sgu.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final PasswordUtil passwordUtil;
    private final EmailEventProducer emailEventProducer;
    private final AuthPermissionValidator authPermissionValidator;

    @Override
    public RegisterResponseDto register(RegisterRequestDto dto) {
        String email = dto.getEmail().trim();
        String password = dto.getPassword().trim();

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        String encodedPassword = passwordEncoder.encode(password);

        User user = UserMapper.toEntity(dto, encodedPassword);

        User registeredUser = userRepository.save(user);

        emailEventProducer.sendWelcomeEmail(email);

        return UserMapper.toRegisterResponseDto(registeredUser);
    }

    @Override
    public LoginResponseDto login(LoginRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        String id = String.valueOf(user.getId());
        String email = user.getEmail();
        String password = user.getPassword();
        String role = user.getRole().name();
        Status status = user.getStatus();

        if (status.equals(Status.INACTIVE)) {
            throw new InvalidCredentialsException("Your account has been locked");
        }

        if (!passwordEncoder.matches(dto.getPassword().trim(), password)) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        if (!role.equals(dto.getRole())) {
            throw new InvalidCredentialsException("Invalid email or password for the specified role");
        }

        String token = jwtUtil.generateToken(id, email, role);

        return UserMapper.toLoginResponseDto(user, token);
    }

    @Override
    public void forgotPassword(ForgotPasswordRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String newPassword = passwordUtil.generateRandomPassword();
        String encodedPassword = passwordEncoder.encode(newPassword);

        user.setPassword(encodedPassword);
        userRepository.save(user);

        emailEventProducer.sendForgotPasswordEmail(dto.getEmail(), newPassword);
    }

    @Override
    public void changePassword(ChangePasswordRequestDto dto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(dto.getOldPassword().trim(), user.getPassword())) {
            throw new InvalidCredentialsException("Old password is incorrect");
        }

        String newPassword = passwordEncoder.encode(dto.getNewPassword().trim());

        user.setPassword(newPassword);
        userRepository.save(user);
    }

    @Override
    public void lockUser(UUID targetId, String role) {
        User user = userRepository.findById(targetId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        authPermissionValidator.validateLockPermission(role);

        user.setStatus(Status.INACTIVE);

        userRepository.save(user);
    }

    @Override
    public void unlockUser(UUID targetId, String role) {
        User user = userRepository.findById(targetId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        authPermissionValidator.validateUnlockPermission(role);

        user.setStatus(Status.ACTIVE);

        userRepository.save(user);
    }
}
