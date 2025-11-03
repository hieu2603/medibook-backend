package com.sgu.auth_service.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class PasswordUtil {

    public String generateRandomPassword() {
        return RandomStringUtils.random(10, 0, 0, true, true, null, new SecureRandom());
    }
}
