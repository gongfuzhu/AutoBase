package com.gongfuzhu.autotools.core.server;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Log4j2
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * 邮箱配置
     * spring:
     *   mail:
     *     host: smtp.example.com
     *     port: 587
     *     username: your-username
     *     password: your-password
     *     protocol: smtp
     *     properties:
     *       mail.smtp.auth: true
     *       mail.smtp.starttls.enable: true
     * @param to
     * @param subject
     * @param text
     */
    public void sendSimpleMail(String from, String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }


}

