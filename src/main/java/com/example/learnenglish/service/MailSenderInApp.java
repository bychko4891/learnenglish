package com.example.learnenglish.service;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailSenderInApp {

    @Value("${spring.mail.username}")
    private String username;


    private final JavaMailSender javaMailSender;

    public MailSenderInApp(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendSimpleMessage(String emailTo, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(username);
        message.setTo(emailTo);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);

    }
}
