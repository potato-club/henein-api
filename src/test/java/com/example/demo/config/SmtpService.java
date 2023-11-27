//package com.example.demo.config;
//
//import com.example.demo.service.AmazonSMTPService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Map;
//
//@SpringBootTest
//public class SmtpService {
//    @Autowired
//    AmazonSMTPService amazonSMTPService;
//
//    @Test
//    void send() {
//        String to = "kjh@test.com";
//        Map<String, Object> variables = Map.of("data", "안녕하세요");
//
//        amazonSMTPService.send(ariables, to);
//    }
//}
