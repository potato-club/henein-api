package com.example.demo.controller;

import com.example.demo.dto.login.BasicLoginRequestDto;
import com.example.demo.error.ErrorCode;
import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@Api(tags = {"JWT 로그인 Controller"})

public class AuthenticationController {

    private final UserService userService;


    @Operation()
    @GetMapping("/login")
    public ResponseEntity<String> basicLogin(@RequestBody BasicLoginRequestDto basicLoginRequestDto, HttpServletResponse response) {

        return userService.basicLogin(basicLoginRequestDto,response);
    }
    @Operation()
    @PostMapping("/login/register")
    public ResponseEntity<String> basicSignUp(@RequestBody BasicLoginRequestDto basicLoginRequestDto, HttpServletResponse response) {

        return userService.basicSignUp(basicLoginRequestDto,response);
    }
    @Operation(summary = "Authorization에 accessToken, RefreshToken에 refreshToken이 들어있음")
    @GetMapping("/login/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        log.info("Controller -> login/kakao 진입시도 코드: "+code);
        return userService.kakaoLogin(code,response);
    }
    @Operation(summary = "AT를 재발급 받기 위한 API")
    @GetMapping("/refresh")
    public ResponseEntity<?> refreshAT(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
       return userService.refreshAT(request, response);
    }
}
