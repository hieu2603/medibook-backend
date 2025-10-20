package com.sgu.email_service.service;

import com.sgu.email_service.constant.EmailType;
import com.sgu.email_service.dto.EmailMessage;

public interface EmailService {
    void sendEmail(EmailMessage emailMessage, EmailType type);
}
