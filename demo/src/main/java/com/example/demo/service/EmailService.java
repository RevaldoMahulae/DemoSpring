package com.example.demo.service;

import jakarta.mail.MessagingException;

public interface EmailService {
    void sendUserCreationEmail(String userEmail, String hrdEmail, String subject, String body) throws MessagingException;
}
