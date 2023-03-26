package com.example.demo.service;

import com.example.demo.dto.KakaoOAuth2User;
import com.example.demo.entity.UserEntity;
import com.example.demo.jwt.CustomeUserDetails;
import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
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
    public UserDetails loadUserByKakaoOAuth2User(KakaoOAuth2User kakaoOAuth2User, String RT) throws IOException {
        // 받은 정보로 찾고, 정보가 없으면 회원가입으로 갑니다.
        log.info("service 진입");
        UserEntity userEntity = userRepository.findByEmail(kakaoOAuth2User.getKakao_account().getEmail());
        //if 문에서 기존 가입지인지 아닌지 구별
        if (userEntity == null){
            UserEntity user = new UserEntity();
            user.KakaoSignUp(kakaoOAuth2User.getKakao_account().getEmail(),RT);
            userRepository.save(user);
            return new CustomeUserDetails(user);
        }

        // 기존 가입자 처리.
        userEntity.setRefreshToken(RT);
        log.info("유저 엔티티 :"+userEntity);
        userRepository.save(userEntity);
        return new CustomeUserDetails(userEntity);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
