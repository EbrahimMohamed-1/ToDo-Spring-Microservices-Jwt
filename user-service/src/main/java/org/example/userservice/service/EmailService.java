package org.example.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;


    @Value("${spring.mail.username}")
    private String from;

    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("Your OTP Code");
        message.setText("Your verification OTP is: " + otp + "\nThis code will expire in 5 minutes.");
        mailSender.send(message);
    }
    public void sendSecurityAlert(String to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Security Alert");
        message.setText("Your session has been terminated due to suspicious activity. Please reset your password if this wasn't you.");
        mailSender.send(message);
    }

}

