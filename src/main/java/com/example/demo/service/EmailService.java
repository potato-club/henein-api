package com.example.demo.service;

import com.example.demo.entity.UserEntity;
import com.example.demo.enumCustom.UserRole;
import com.example.demo.error.ErrorCode;
import com.example.demo.error.exception.DuplicateException;
import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.UUID;

@Service
@Transactional

public class EmailService {

    private final JavaMailSender naverSender;
    private final JavaMailSender gmailSender;
    private final RedisService redisService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${email.naver.id}")
    private String naverId;
    @Value("${email.gmail.id}")
    private String gmailId;

    @Autowired
    public EmailService(@Qualifier("gmail") JavaMailSender gmailSender,
                            @Qualifier("naver") JavaMailSender naverSender,
                            RedisService redisService,
                            UserRepository userRepository,
                            JwtTokenProvider jwtTokenProvider) {
        this.gmailSender = gmailSender;
        this.naverSender = naverSender;
        this.redisService = redisService;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public void sendGmail(String requestEmail) throws MessagingException, UnsupportedEncodingException {
        if ( userRepository.existsByUserEmail(requestEmail) ) {
            throw new DuplicateException(ErrorCode.DUPLICATE_EMAIL.getMessage(),ErrorCode.DUPLICATE_EMAIL);
        }

        redisService.deleteExistingOtp(requestEmail);
        MimeMessage message = gmailSender.createMimeMessage();
        String OTP = createKey();
        message = commonMessage(requestEmail,message,"gmail",OTP);
        redisService.setEmailOtpDataExpire(requestEmail, OTP, 5);
        gmailSender.send(message);
    }
    public void sendNaverMail(String requestEmail) throws MessagingException, UnsupportedEncodingException {
        if ( userRepository.existsByUserEmail(requestEmail) ) {
            throw new DuplicateException(ErrorCode.DUPLICATE_EMAIL.getMessage(),ErrorCode.DUPLICATE_EMAIL);
        }

        redisService.deleteExistingOtp(requestEmail);
        MimeMessage message = naverSender.createMimeMessage();
        String OTP = createKey();
        message = commonMessage(requestEmail,message,"naver",OTP);
        redisService.setEmailOtpDataExpire(requestEmail, OTP, 5);
        naverSender.send(message);
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

    private String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();
        // 인증코드 6자리
        for (int i = 0; i < 6; i++) {
            key.append((rnd.nextInt(10)));
        }
        return key.toString();
    }
    public MimeMessage commonMessage(String requestEmail, MimeMessage message, String sendType, String OTP) throws MessagingException, UnsupportedEncodingException {

        message.addRecipients(MimeMessage.RecipientType.TO, requestEmail); // to 보내는 대상
        message.setSubject("Henein 인증 코드: "); //메일 제목

        // 메일 내용 메일의 subtype을 html로 지정하여 html문법 사용 가능

        String msg="";
        msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">이메일 주소 확인</h1>";
        msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">아래 확인 코드를 회원가입 화면에서 입력해주세요.</p>";
        msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
        msg += OTP;
        msg += "</td></tr></tbody></table></div>";

        message.setText(msg, "utf-8", "html"); // 내용, charset 타입, subType

        if (sendType == "gmail") {
            message.setFrom(new InternetAddress(gmailId,"Henein_Admin")); // 보내는 사람의 메일 주소, 보내는 사람 이름
        } else {
            message.setFrom(new InternetAddress(naverId,"Henein_Admin"));
        }

        return message;
    }
}
