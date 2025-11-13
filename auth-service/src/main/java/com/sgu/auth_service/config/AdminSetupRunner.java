package com.sgu.auth_service.config;

import com.sgu.auth_service.constant.Role;
import com.sgu.auth_service.model.User;
import com.sgu.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminSetupRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String adminEmail = "nguyenvanhieu2603@gmail.com";
        String adminPassword = "admin123";

        boolean adminExists = userRepository.existsByEmail(adminEmail);

        if (!adminExists) {
            User admin = User.builder()
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .role(Role.ADMIN)
                    .build();

            userRepository.save(admin);
            log.info("Admin created with email={}, password={}", adminEmail, adminPassword);
        } else {
            log.info("Admin already exists");
        }
    }
}
