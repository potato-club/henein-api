package com.example.demo.controller;

import com.example.demo.dto.login.BasicLoginRequestDto;
import com.example.demo.service.AmazonSMTPService;
import com.example.demo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@Api(tags = {"JWT 로그인 Controller"})

public class AuthenticationController {

    private final UserService userService;
    private final AmazonSMTPService amazonSMTPService;


    //=======Local 로그인 관련
    @Operation(summary = "로컬 로그인 userEmail,password")
    @PostMapping("/login")
    public ResponseEntity<String> basicLogin(@RequestBody BasicLoginRequestDto basicLoginRequestDto, HttpServletResponse response) {
        return userService.basicLogin(basicLoginRequestDto,response);
    }
    @Operation(summary = "로컬 회원가입 userEmail,password")
    @PostMapping("/login/register")
    public ResponseEntity<String> basicSignUp(@RequestBody BasicLoginRequestDto basicLoginRequestDto, HttpServletRequest request) {
        return userService.basicSignUp(basicLoginRequestDto,request);
    }
    @Operation(summary = "인증 메일 발송 요청")
    @PostMapping("/mail/naver")
    public ResponseEntity<String> sendNaver(String requestEmail) {
        amazonSMTPService.sendToVerifyEmail(requestEmail);
        return ResponseEntity.ok("인증 메일이 발송되었습니다.");
    }
    @Operation(summary = "인증 코드 확인 API")
    @PostMapping("/verify")
    public ResponseEntity<String> verifyEmail(String key, HttpServletResponse response) {
        amazonSMTPService.verifyEmailAuth(key, response);
        return ResponseEntity.ok("이메일 인증이 정상적으로 처리되었습니다.");
    }


    //=====OAuth2 관련========

    @Operation(summary = "Authorization에 accessToken, RefreshToken에 refreshToken이 들어있음")
    @GetMapping("/login/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestParam("code") String code, HttpServletResponse response) {
        return userService.kakaoLogin(code,response);
    }
    @Operation(summary = "AT를 재발급 받기 위한 API")
    @GetMapping("/refresh")
    public ResponseEntity<?> refreshAT(HttpServletRequest request, HttpServletResponse response) {
       return userService.refreshAT(request, response);
    }
}
