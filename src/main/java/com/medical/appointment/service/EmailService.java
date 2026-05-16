package com.medical.appointment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Async
    public void sendVerificationEmail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject("Verify Your Hospital Account");
        message.setText(
                "Welcome to our Medical Appointment System!\n\n" +
                        "Your verification code is: " + code + "\n" +
                        "This code will expire in 5 minutes.\n\n" +
                        "If you did not register for this account, please ignore this email."
        );

        try {
            mailSender.send(message);
            log.info("Email successfully sent to: {}", to);
        } catch (MailException e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage(), e);
        }
    }

    @Async
    public void sendPasswordResetEmail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject("Reset Your Hospital Account Password");
        message.setText(
                "We received a request to reset your password.\n\n" +
                        "Your password reset code is: " + code + "\n" +
                        "This code will expire in 5 minutes.\n\n" +
                        "If you did not request this, please ignore this email."
        );

        try {
            mailSender.send(message);
            log.info("Password reset email successfully sent to: {}", to);
        } catch (MailException e) {
            log.error("Failed to send password reset email to {}: {}", to, e.getMessage(), e);
        }
    }
}