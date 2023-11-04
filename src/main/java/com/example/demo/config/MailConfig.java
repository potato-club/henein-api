package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Value("${email.naver.id}")
    private String naverId;
    @Value("${email.naver.pw}")
    private String naverPw;
    @Value("${email.gmail.id}")
    private String gmailId;
    @Value("${email.gmail.pw}")
    private String gmailPw;

    @Bean(name ="naver")
    public JavaMailSender naverJavaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost("smtp.naver.com"); // 메인 도메인 서버 주소 => 정확히는 smtp 서버 주소
        javaMailSender.setUsername(naverId); // 네이버 아이디
        javaMailSender.setPassword(naverPw); // 네이버 비밀번호
        javaMailSender.setPort(465); // 메일 인증서버 포트
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp"); // 프로토콜 설정
        properties.setProperty("mail.smtp.auth", "true"); // smtp 인증
        properties.setProperty("mail.smtp.starttls.enable", "true"); // smtp strattles 사용
        properties.setProperty("mail.debug", "true"); // 디버그 사용
        properties.setProperty("mail.smtp.ssl.trust","smtp.naver.com"); // ssl 인증 서버는 smtp.naver.com
        properties.setProperty("mail.smtp.ssl.enable","true"); // ssl 사용


        return javaMailSender;
    }
    @Bean(name = "gmail")
    public JavaMailSender gmailJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(gmailId);
        mailSender.setPassword(gmailPw);
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        return mailSender;
    }


}
