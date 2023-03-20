package com.example.demo.service;

import com.example.demo.dto.KakaoOAuth2User;
import com.example.demo.entity.UserEntity;
import com.example.demo.jwt.CustomeUserDetails;
import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;

/**
* 이 클래스는 UserDetailsService 인터페이스를 구현하며,
*  Kakao 인증 정보를 사용하여 사용자를 찾거나 생성하고 인증 객체를 생성하는 역할을 합니다.
*  이 클래스에서는 Kakao 인증 정보를 사용하여 사용자를 찾거나 생성하고,
*  JWT 토큰을 생성한 후 사용자에게 반환합니다.*/
@Service
@Slf4j
@RequiredArgsConstructor
public class KakaoOAuth2UserDetailsServcie implements UserDetailsService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    @Transactional
    public UserDetails loadUserByKakaoOAuth2User(KakaoOAuth2User kakaoOAuth2User, String AT, String RT, HttpServletResponse response) throws IOException {
        // 받은 정보로 찾고, 정보가 없으면 회원가입으로 갑니다.
        log.info("service 진입");
        UserEntity userEntity = userRepository.findByEmail(kakaoOAuth2User.getEmail());
        if (userEntity == null){
            String redirect_uri = "https://www.henesysBack.com/auto/register";
            response.sendRedirect(redirect_uri);
            return null;
        }
        // Generate JWT tokens
        String accessToken = jwtTokenProvider.generateAccessToken(userEntity.getUsername());
        String refreshToken = jwtTokenProvider.generateRefreshToken(userEntity.getUsername());
        // Set tokens to user
        userEntity.setToken(accessToken,refreshToken);
        log.info("유저 엔티티 :"+userEntity);
        return new CustomeUserDetails(userEntity);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
