package com.sgu.email_service.service.impl;

import com.sgu.email_service.constant.EmailType;
import com.sgu.email_service.dto.EmailMessage;
import com.sgu.email_service.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Override
    public void sendEmail(EmailMessage emailMessage, EmailType type) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

            Context context = new Context();
            context.setVariables(emailMessage.getVariables());

            String template = type.getTemplate();
            String htmlContent = templateEngine.process(template, context);

            helper.setTo(emailMessage.getTo());
            helper.setSubject(type.getSubject());
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);

            log.info("Email [{}] sent to : {}", template, emailMessage.getTo());
        } catch (MessagingException e) {
            log.error("Failed to send email: {}", e.getMessage());
        }
    }
}
