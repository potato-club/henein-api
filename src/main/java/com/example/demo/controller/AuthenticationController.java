package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.entity.UserEntity;
import com.example.demo.jwt.CustomeUserDetails;
import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.jwt.KakaoOAuth2AccessTokenResponse;
import com.example.demo.jwt.KakaoOAuth2Client;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.KakaoOAuth2UserDetailsServcie;
import com.example.demo.service.UserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@Api(tags = {"JWT 로그인 Controller"})
public class AuthenticationController {

    private final UserService userService;
    private final KakaoOAuth2UserDetailsServcie kakaoOAuth2UserDetailsServcie;
    private final KakaoOAuth2Client kakaoOAuth2Client;
    private final JwtTokenProvider jwtTokenProvider;


    @PostMapping("/login/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws IOException {
        log.info("Controller -> login/kakao 진입시도 코드: "+code);

        KakaoOAuth2AccessTokenResponse tokenResponse = kakaoOAuth2Client.getAccessToken(code);

        // 카카오 사용자 정보를 가져옵니다.
        KakaoOAuth2User kakaoOAuth2User = kakaoOAuth2Client.getUserProfile(tokenResponse.getAccessToken());
        log.info("카카오 사용자 정보를 가져옵니다 kakaoOAuth2User:"+kakaoOAuth2User);

        // 사용자 정보를 기반으로 우리 시스템에 인증을 수행합니다.
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                kakaoOAuth2User, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("사용자 정보를 기반으로 우리 시스템에 인증을 수행 authentication:"+authentication);

        // JWT 토큰을 발급합니다.
        String accessToken = jwtTokenProvider.generateAccessToken(authentication.getName());
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication.getName());

        // 로그인한 사용자의 정보를 저장합니다.
        kakaoOAuth2UserDetailsServcie.loadUserByKakaoOAuth2User(kakaoOAuth2User,
                tokenResponse.getAccessToken(), tokenResponse.getRefreshToken(),response);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token","Bearer"+accessToken);
        tokens.put("refresh_token","Bearer"+refreshToken);
        return ResponseEntity.ok(tokens);

    }
    //////

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
       return userService.login(loginRequest, response);
    }
    @GetMapping("/refresh")
    public ResponseEntity<?> refreshAT(@RequestHeader("Authorization") String RTHeader,HttpServletResponse response) {
       return userService.refreshAT(RTHeader, response);
    }
    @PostMapping("/register")
    public String register(@RequestBody UserRegisterRequest userRegisterRequest){
        return userService.registerUser(userRegisterRequest);
    }


}
