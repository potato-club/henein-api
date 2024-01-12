package com.example.demo.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import com.example.demo.enumCustom.UserRole;
import com.example.demo.error.ErrorCode;
import com.example.demo.error.exception.ForbiddenException;
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

@Service
@RequiredArgsConstructor
public class AmazonSMTPService {
    private final AmazonSimpleEmailService amazonSimpleEmailService;
    private final TemplateEngine htmlTemplateEngine;
    private final RedisService redisService;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${aws.ses.from}")
    private String from;

    public void sendToVerifyEmail(String requestEmail) {
        //이미 인증메일을 보냈고 검증까지 마친 이메일이니?
        if (redisService.emailIsAlreadyReadied(requestEmail)) {
            throw new ForbiddenException(ErrorCode.ALREADY_EXISTS.getMessage(), ErrorCode.ALREADY_EXISTS);
        }

        String OTP = createOTP();
        Map<String, Object> variables = new HashMap<>();
        variables.put("OTP", OTP);
        variables.put("imageUrl","https://henesysbucket.s3.ap-northeast-2.amazonaws.com/abcdcba-henein-logo.svg");

        String content = htmlTemplateEngine.process("mailTemplate", createContext(variables));

        SendEmailRequest sendEmailRequest = createSendEmailRequest("Henein 로그인 인증", content, requestEmail);

        amazonSimpleEmailService.sendEmail(sendEmailRequest);

        redisService.setEmailOtpDataExpire(requestEmail, OTP, 5);
    }
    public void verifyEmailAuth(String OTP, HttpServletResponse response) {
        String email = redisService.getEmailOtpData(OTP);

        String AT = jwtTokenProvider.generateAccessToken(email, UserRole.USER);

        response.setHeader("Authorization","Bearer " + AT);

        redisService.deleteEmailOtpData(OTP);
        redisService.setReadyEmailForSignUp(email,AT);

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