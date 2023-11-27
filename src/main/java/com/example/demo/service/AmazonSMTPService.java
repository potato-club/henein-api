package com.example.demo.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import com.example.demo.entity.UserEntity;
import com.example.demo.enumCustom.UserRole;
import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AmazonSMTPService {
    private final AmazonSimpleEmailService amazonSimpleEmailService;
    private final TemplateEngine htmlTemplateEngine;
    private final RedisService redisService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Value("${aws.ses.from}")
    private String from;

    public void sendToVerifyEmail(String requestEmail) {
        String OTP = createOTP(); // Keep OTP generation logic
        Map<String, Object> variables = new HashMap<>();
        variables.put("OTP", OTP); // Add OTP to the variables map

        String content = htmlTemplateEngine.process("mailTemplate", createContext(variables));

        SendEmailRequest sendEmailRequest = createSendEmailRequest("Henein 로그인 인증", content, requestEmail);

        amazonSimpleEmailService.sendEmail(sendEmailRequest);

        redisService.setEmailOtpDataExpire(requestEmail, OTP, 5);
    }
    public void verifyEmailAuth(String OTP, HttpServletResponse response) {
        String email = redisService.getEmailOtpData(OTP);

        String AT = jwtTokenProvider.generateAccessToken(email);
        String RT = jwtTokenProvider.generateRefreshToken(email);

        response.setHeader("Authorization","Bearer " + AT);
        response.setHeader("RefreshToken","Bearer " + RT);

        UserEntity userEntity = UserEntity.builder()
                .userEmail(email)
                .userName("ㅇㅇ")
                .refreshToken(RT)
                .userRole(UserRole.USER)
                .uid(String.valueOf(UUID.randomUUID()))
                .build();

        userRepository.save(userEntity);

        redisService.deleteEmailOtpData(OTP);

    }

    private Context createContext(Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);

        return context;
    }

    private SendEmailRequest createSendEmailRequest(String subject, String content, String... to) {
        return new SendEmailRequest()
                .withDestination(new Destination().withToAddresses(to))
                .withSource(from)
                .withMessage(new Message()
                        .withSubject(new Content().withCharset(StandardCharsets.UTF_8.name()).withData(subject))
                        .withBody(new Body().withHtml(new Content().withCharset(StandardCharsets.UTF_8.name()).withData(content)))
                );
    }
    private String createOTP() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();
        // 인증코드 6자리
        for (int i = 0; i < 6; i++) {
            key.append((rnd.nextInt(10)));
        }
        return key.toString();
    }
}