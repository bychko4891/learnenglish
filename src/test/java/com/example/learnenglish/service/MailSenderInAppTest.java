package com.example.learnenglish.service;

/**
 * @author: Artur Hasparian
 * Application Name: Learn English
 * Description: Unit test
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


class MailSenderInAppTest {
    @Mock
    private JavaMailSender javaMailSender;
    @InjectMocks
    private MailSenderInApp mailSenderInApp;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mailSenderInApp = new MailSenderInApp(javaMailSender);
    }

    @Test
    void sendSimpleMessage() {
        var email = "learnenglish@example.com";
        var subject = "Test subject";
        var text = "Test Message";

        mailSenderInApp.sendSimpleMessage(email,subject,text);

        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));

    }
}