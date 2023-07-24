package com.example.demo.service;

import com.example.demo.entity.GuestCountEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.enumCustom.UserRole;
import com.example.demo.jwt.CustomeUserDetails;
import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.repository.GuestCountRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
    private final GuestCountRepository guestCountRepository;
    @Transactional
    public UserDetails loadUserByKakaoOAuth2User(String email, String RT) {
        // 받은 정보로 찾고, 정보가 없으면 회원가입으로 갑니다.
        log.info("DB저장 service 진입: "+ email);
        UserEntity userEntity = userRepository.findByUserEmail(email)
                .orElseGet(() ->controlUser(email));

        userEntity.setRefreshToken(RT);
        userRepository.save(userEntity);

        return new CustomeUserDetails(userEntity);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
    public UserEntity controlUser(String email){
        GuestCountEntity guestCount = guestCountRepository.getById(new Long(1));
        guestCount.addCount();
        //유저이름을 "guest" + guestCount로 설정
        UserEntity userEntity = new UserEntity(email, guestCount.getGuestCount());
        guestCountRepository.save(guestCount);
        return userEntity;
    }
}
