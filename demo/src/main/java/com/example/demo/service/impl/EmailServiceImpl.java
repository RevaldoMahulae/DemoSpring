package com.example.demo.service.impl;

import com.example.demo.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;
    
    @Override
    public void sendUserCreationEmail(String userEmail, String hrdEmail, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("revaldo.mahulae@qualitas.co.id");
        helper.setCc(userEmail);
        helper.setTo(hrdEmail); 
        helper.setSubject(subject);
        helper.setText(body, true);

        mailSender.send(message);
    }
}
