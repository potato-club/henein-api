package com.example.demo.controller;

import com.example.demo.dto.login.BasicLoginRequestDto;
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


    @Operation(summary = "로컬 로그인 userEmail,password")
    @PostMapping("/login")
    public ResponseEntity<String> basicLogin(@RequestBody BasicLoginRequestDto basicLoginRequestDto, HttpServletResponse response) {
        return userService.basicLogin(basicLoginRequestDto,response);
    }
    @Operation(summary = "로컬 회원가입 userEmail,password")
    @PostMapping("/login/register")
    public ResponseEntity<String> basicSignUp(@RequestBody BasicLoginRequestDto basicLoginRequestDto, HttpServletResponse response) {
        return userService.basicSignUp(basicLoginRequestDto,response);
    }

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
