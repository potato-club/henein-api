package com.example.demo.controller;

import com.example.demo.error.ErrorCode;
import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@Api(tags = {"JWT 로그인 Controller"})
public class AuthenticationController {

    private final UserService userService;

    private final JwtTokenProvider jwtTokenProvider;


    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, HttpServletResponse response) {
        String accessToken = jwtTokenProvider.generateAccessToken(email);
        String refreshToken = jwtTokenProvider.generateRefreshToken(email);

        response.setHeader("Authorization","Bearer " + accessToken);
        response.setHeader("RefreshToken","Bearer "+ refreshToken);

        return ResponseEntity.ok("로그인 성공");
    }
    @Tag(name ="카카오 소셜로그인",description = "Authorization에 accessToken, RefreshToken에 refreshToken이 들어있음")
    @GetMapping("/login/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        log.info("Controller -> login/kakao 진입시도 코드: "+code);
        return userService.kakaoLogin(code,response);
    }
    @GetMapping("/refresh")
    public ResponseEntity<?> refreshAT(HttpServletRequest request, HttpServletResponse response) {
       return userService.refreshAT(request, response);
    }
}
