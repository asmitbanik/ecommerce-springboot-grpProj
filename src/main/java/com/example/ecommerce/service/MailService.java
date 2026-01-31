package com.example.ecommerce.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private final JavaMailSender mailSender;
    private final Logger log = LoggerFactory.getLogger(MailService.class);

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendRegistrationEmail(String to, String username) {
        // For mock/testing we just log and attempt to send via configured mail sender (which can be a mock SMTP)
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("Welcome to E-commerce");
        msg.setText("Hello " + username + ",\n\nThank you for registering.");
        try {
            mailSender.send(msg);
            log.info("Sent registration email to {}", to);
        } catch (Exception ex) {
            log.warn("Mail send failed (mock mode) to {}: {}", to, ex.getMessage());
        }
    }
}