package com.at.register.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private MailSender mailSender;

    public Boolean triggerEmail(String to, String from, String context) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText(context);
        message.setTo(to);
        message.setFrom(from);
        try {
            mailSender.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
