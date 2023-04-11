package com.example.demo.service;

import com.example.demo.dto.login.KakaoOAuth2User;
import com.example.demo.dto.login.LoginRequest;
import com.example.demo.dto.login.TokenResponse;
import com.example.demo.dto.login.UserRegisterRequest;
import com.example.demo.dto.user.UserInfoResponseDto;
import com.example.demo.entity.UserEntity;
import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.jwt.KakaoOAuth2AccessTokenResponse;
import com.example.demo.jwt.KakaoOAuth2Client;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoOAuth2UserDetailsServcie kakaoOAuth2UserDetailsServcie;
    private final KakaoOAuth2Client kakaoOAuth2Client;

    @Transactional
    public UserInfoResponseDto userInfo(HttpServletRequest request){
        String AT = jwtTokenProvider.resolveAccessToken(request);
        String userEmail = jwtTokenProvider.getUserEmailFromAccessToken(AT); // 정보 가져옴
        UserEntity userEntity = userRepository.findByEmail(userEmail).
                orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + userEmail));
        return new UserInfoResponseDto(userEntity);
    }

    @Transactional
    public ResponseEntity<?> kakaoLogin(String code, HttpServletResponse response) throws IOException {
        KakaoOAuth2AccessTokenResponse tokenResponse = kakaoOAuth2Client.getAccessToken(code);
        // 카카오 사용자 정보를 가져옵니다.
        KakaoOAuth2User kakaoOAuth2User = kakaoOAuth2Client.getUserProfile(tokenResponse.getAccessToken());
        log.info("카카오 사용자 정보를 가져옵니다 kakaoOAuth2User:"+kakaoOAuth2User.getKakao_account().getEmail());

        // 사용자 정보를 기반으로 우리 시스템에 인증을 수행합니다.
        Authentication authentication = new UsernamePasswordAuthenticationToken(kakaoOAuth2User, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // JWT 토큰을 발급합니다.
        String email = kakaoOAuth2User.getKakao_account().getEmail();
        log.info("JWT 토큰을 발급합니다 Controller: "+email);
        String accessToken = jwtTokenProvider.generateAccessToken(email);
        String refreshToken = jwtTokenProvider.generateRefreshToken(email);

        // 로그인한 사용자의 정보를 저장합니다.
        kakaoOAuth2UserDetailsServcie.loadUserByKakaoOAuth2User(email, refreshToken);

        //클라이언트에게 리턴해주기
        response.setHeader("Authorization","Bearer " + accessToken);
        response.setHeader("RefreshToken","Bearer " + refreshToken);

        Map<String, String> tokens =new HashMap<>();
        tokens.put("access_token","Bearer " + accessToken);
        tokens.put("refresh_token","Bearer " + refreshToken);

        return ResponseEntity.ok(tokens);
    }
    @Transactional
    public ResponseEntity<?> refreshAT(String RTHeader,HttpServletResponse response){
        //bearer 지우기
        String RT = RTHeader.substring(7);

        try {
            // Validate the refreshToken and generate a new accessToken
            String newAccessToken = jwtTokenProvider.refreshAccessToken(RTHeader);

            // Set the new access token in the HTTP response headers
            response.setHeader("Authorization", "Bearer " + newAccessToken);

            // Optionally, return the new access token in the response body as well
            return ResponseEntity.ok(newAccessToken);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
    @Transactional
    public String registerUser(UserRegisterRequest userRegisterRequest){
        //이미 있는 이름인지 확인
        if (userRepository.existsByUsername(userRegisterRequest.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }
        // Create a new user and Save
        userRegisterRequest.setPassword(passwordEncoder.encode(userRegisterRequest.getPassword()));
        userRepository.save(userRegisterRequest.toEntity(userRegisterRequest));

        return "저장 완료";

    }
}
