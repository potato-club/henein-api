package com.example.demo.controller;

import com.example.demo.dto.login.KakaoOAuth2User;
import com.example.demo.error.ErrorCode;
import com.example.demo.error.exception.CustomException;
import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.jwt.KakaoOAuth2AccessTokenResponse;
import com.example.demo.jwt.KakaoOAuth2Client;
import com.example.demo.service.KakaoOAuth2UserDetailsServcie;
import com.example.demo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@Api(tags = {"JWT 로그인 Controller"})
@CrossOrigin(origins = "http://localhost:3000")
public class AuthenticationController {

    private final UserService userService;

    private final JwtTokenProvider jwtTokenProvider;


    @GetMapping("/errortest")
    public ResponseEntity<String> testError() {
        throw new CustomException(ErrorCode.INVALID_TOKEN, ErrorCode.INVALID_TOKEN.getMessage());
    }
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
    public ResponseEntity<?> refreshAT(@RequestHeader("RefreshToken") String RTHeader,HttpServletResponse response) {
       return userService.refreshAT(RTHeader, response);
    }

}
