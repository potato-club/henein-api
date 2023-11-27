package com.example.demo.config;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import com.example.demo.entity.UserEntity;
import com.example.demo.enumCustom.UserRole;
import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class AwsSESSender {
    private final AmazonSimpleEmailService amazonSimpleEmailService;
    private final RedisService redisService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Async
    public void sendToVerifyEmail(String requestEmail) {
        try {
            // 이메일 HTML 파일 위치를 통해 HTML 파일 가져오기
            ClassPathResource mailTemplate = new ClassPathResource("templates/mailTemplate.html");

            // 라이브러리를 이용하여 HTML 파일 내용을 String으로 변환
            String mailContent = new BufferedReader(
                    new InputStreamReader(mailTemplate.getInputStream(), StandardCharsets.UTF_8)
            ).lines().collect(Collectors.joining("\n"));

            // 실제 인증코드 삽입
            String OTP = createOTP();
            mailContent = mailContent.replace("[OTP]",OTP);

            // 이메일 전송
            SendEmailRequest request = generateSendEmailRequest(requestEmail, mailContent);
            amazonSimpleEmailService.sendEmail(request);
            redisService.setEmailOtpDataExpire(requestEmail,OTP,5);
        } catch (IOException e) {
            log.error("이메일 생성 중 문제가 발생했습니다. error message = {}", e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }
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
    @Async
    protected SendEmailRequest generateSendEmailRequest(String requestEmail, String mailContent) {
        Destination destination = new Destination().withToAddresses(requestEmail);
        Message message = new Message()
                .withSubject(createContent("Henein 로그인 인증"))
                .withBody(new Body()
                        .withHtml(createContent(mailContent))
                );
        return new SendEmailRequest()
                .withDestination(destination)
                .withSource("message@henein.kr")
                .withMessage(message);
    }
    @Async
    protected Content createContent(String text) {
        return new Content()
                .withCharset("UTF-8")
                .withData(text);
    }
    @Async
    protected String createOTP() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();
        // 인증코드 6자리
        for (int i = 0; i < 6; i++) {
            key.append((rnd.nextInt(10)));
        }
        return key.toString();
    }
}
