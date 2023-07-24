package com.example.demo.service;

import com.example.demo.dto.userchar.NodeConnection;
import com.example.demo.dto.userchar.UserMapleApi;
import com.example.demo.dto.login.BasicLoginRequestDto;
import com.example.demo.dto.login.KakaoOAuth2User;

import com.example.demo.dto.user.UserInfoResponseDto;
import com.example.demo.dto.user.UserNicknameChange;
import com.example.demo.entity.GuestCountEntity;
import com.example.demo.entity.UserCharEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.enumCustom.UserRole;
import com.example.demo.error.ErrorCode;
import com.example.demo.error.exception.AuthenticationException;
import com.example.demo.error.exception.DuplicateException;
import com.example.demo.error.exception.NotFoundException;
import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.jwt.KakaoOAuth2AccessTokenResponse;
import com.example.demo.jwt.KakaoOAuth2Client;
import com.example.demo.repository.GuestCountRepository;
import com.example.demo.repository.UserCharRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserCharRepository userCharRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoOAuth2UserDetailsServcie kakaoOAuth2UserDetailsServcie;
    private final KakaoOAuth2Client kakaoOAuth2Client;
    private final GuestCountRepository guestCountRepository;
    private final PasswordEncoder passwordEncoder;
    private final WebClient infoWebClient;
    private final WebClient cubeWebClient;
    private final RedisService redisService;


//=================필터사용
    @Transactional
    public UserEntity fetchUserEntityByHttpRequest(HttpServletRequest request, HttpServletResponse response){
        try {
            String AT = jwtTokenProvider.resolveAccessToken(request);

            String userEmail = jwtTokenProvider.getUserEmailFromAccessToken(AT); // 정보 가져옴
            UserEntity userEntity = userRepository.findByUserEmail(userEmail).
                    orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + userEmail));
            return userEntity;
        }catch (NullPointerException e){
            throw new NullPointerException(e.getMessage());
        }
    }
    @Transactional
    public ResponseEntity<?> refreshAT(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException {
        //bearer 지우기
        String RTHeader = jwtTokenProvider.resolveRefreshToken(request);

        // rt 넣어서 검증하고 유저이름 가져오기 /
        String userEmail = jwtTokenProvider.refreshAccessToken(RTHeader ,response);
        UserEntity userEntity = userRepository.findByUserEmail(userEmail).orElseThrow(()->{throw new RuntimeException();});
        //db에 있는 토큰값과 넘어온 토큰이 같은지
        if (!userEntity.getRefreshToken().equals(RTHeader)){
            response.addHeader("exception", String.valueOf(ErrorCode.INVALID_TOKEN.getCode()));
            throw new AuthenticationException(ErrorCode.INVALID_TOKEN);
        }
        String newAccessToken = jwtTokenProvider.generateAccessToken(userEmail);

        // Set the new access token in the HTTP response headers
        response.setHeader("Authorization", "Bearer " + newAccessToken);

        // Optionally, return the new access token in the response body as well
        return ResponseEntity.ok(newAccessToken);
    }
    //===============마이페이지 관련
    @Transactional
    public UserInfoResponseDto userInfo(HttpServletRequest request, HttpServletResponse response){
        UserEntity userEntity = fetchUserEntityByHttpRequest(request,response);
        return new UserInfoResponseDto(userEntity);
    }
    @Transactional
    public String userNicknameChange(HttpServletRequest request, HttpServletResponse response, UserNicknameChange userNickname) throws UnsupportedEncodingException {
        UserEntity userEntity = fetchUserEntityByHttpRequest(request, response);
        userEntity.Update(userNickname);
        userRepository.save(userEntity);
        return "유저 이름 설정 완료";
    }


    public Mono<List<String>> requestToNexon(HttpServletRequest request,UserMapleApi userMapleApi){
        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        UserEntity userEntity = userRepository.findByUserEmail(jwtTokenProvider.getUserEmailFromAccessToken(accessToken))
                .orElseThrow(()->{throw new NotFoundException(ErrorCode.NOT_FOUND,ErrorCode.NOT_FOUND.getMessage());
        });
        return this.cubeWebClient.post()
                .body(BodyInserters.fromValue(userMapleApi))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .flatMap(result -> {
                    List<UserCharEntity> userCharEntityList = new ArrayList<>();
                    for(int i = 0; i < result.size(); i++){
                        userCharEntityList.add(new UserCharEntity(userEntity,result.get(i)));
                    }
                    userCharRepository.saveAll(userCharEntityList);
                    return Mono.just(result);
                });
    }
    @Transactional
    public String requestUpdateToNode(String userCharName){
        UserCharEntity userCharEntity = userCharRepository.findByNickName(userCharName)
                .orElseThrow(()->{throw new NotFoundException(ErrorCode.NULL_VALUE,ErrorCode.NULL_VALUE.getMessage());});
        //요청 보내기전에 1시간 시간 제한 걸어야함 레디스 유효시간 1시간임
        if (null != redisService.checkRedis(userCharName))
            throw new RuntimeException(); // 몇분 남았는지도 알려줘야함

        // 요청 보내기
        String callback = "https://henesysback.shop/userinfo/character/info";
        String result = this.infoWebClient.put()
                .uri(userCharName)
                .body(BodyInserters.fromValue(callback))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        //거절시 null로 오나? 확인해야함
        if (result == null){
            throw new RuntimeException();
        }
        return redisService.setWorkStatus(userCharName);
    }
    @Transactional
    public String responseToRedisAndUpdate(NodeConnection nodeConnection){
        if (!userCharRepository.existsByNickName(nodeConnection.getCharacter().getNickname())){
            throw new NotFoundException(ErrorCode.NULL_VALUE,ErrorCode.NULL_VALUE.getMessage());
        }
        UserCharEntity userCharEntity = userCharRepository.findByNickName(nodeConnection.getCharacter().getNickname())
                .orElseThrow(()->{throw new NotFoundException(ErrorCode.NOT_FOUND,ErrorCode.NOT_FOUND.getMessage());});

        userCharEntity.update(nodeConnection.getCharacter());

        return redisService.updateWork(nodeConnection.getCharacter().getNickname());
    }

    //==================로그인 관련
    @Transactional
    public ResponseEntity<String> basicLogin(BasicLoginRequestDto basicLoginRequestDto, HttpServletResponse response){
        UserEntity userEntity = userRepository.findByUserEmail(basicLoginRequestDto.getUserEmail()).orElseThrow(()->{
            throw new AuthenticationException(ErrorCode.INVALID_USER,ErrorCode.INVALID_USER.getMessage());});

        if ( !passwordEncoder.matches(basicLoginRequestDto.getPassword(),userEntity.getPassword()) ){
            throw new AuthenticationException(ErrorCode.INVALID_USER,ErrorCode.INVALID_USER.getMessage());
        }

        String accessToken = jwtTokenProvider.generateAccessToken(basicLoginRequestDto.getUserEmail());
        String refreshToken = jwtTokenProvider.generateRefreshToken(basicLoginRequestDto.getUserEmail());
        userEntity.setRefreshToken(refreshToken);
        response.setHeader("Authorization","Bearer " + accessToken);
        response.setHeader("RefreshToken","Bearer "+ refreshToken);
        return ResponseEntity.ok("로그인 성공");
    }

    @Transactional
    public ResponseEntity<String> basicSignUp(BasicLoginRequestDto basicLoginRequestDto, HttpServletResponse response){
        //이미 있는 이메일인지 확인
        if (userRepository.existsByUserEmail(basicLoginRequestDto.getUserEmail())){
            throw new DuplicateException(ErrorCode.DUPLICATE_EMAIL,ErrorCode.DUPLICATE_EMAIL.getMessage());
        }

        //토큰 발급
        String accessToken = jwtTokenProvider.generateAccessToken(basicLoginRequestDto.getUserEmail());
        String refreshToken = jwtTokenProvider.generateRefreshToken(basicLoginRequestDto.getUserEmail());

        //디비 저장
        GuestCountEntity guestCount = guestCountRepository.getById(new Long(1));
        guestCount.addCount();
        UserEntity userEntity = new UserEntity().builder()
                .userRole(UserRole.USER)
                .userName("guest"+guestCount.getGuestCount())
                .userEmail(basicLoginRequestDto.getUserEmail())
                .password(passwordEncoder.encode(basicLoginRequestDto.getPassword()))
                .refreshToken(refreshToken)
                .build();
        userRepository.save(userEntity);

        response.setHeader("Authorization","Bearer " + accessToken);
        response.setHeader("RefreshToken","Bearer "+ refreshToken);
        return ResponseEntity.ok("회원가입 성공");
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
        String existsUser ="신규 유저입니다.";
        Map<String, String> tokens =new HashMap<>();
        if (!userRepository.existsByUserEmail(email)){
            tokens.put("status",existsUser);
        }
        // 로그인한 사용자의 정보를 저장합니다.
        kakaoOAuth2UserDetailsServcie.loadUserByKakaoOAuth2User(email, refreshToken);

        //클라이언트에게 리턴해주기
        response.setHeader("Authorization","Bearer " + accessToken);
        response.setHeader("RefreshToken","Bearer " + refreshToken);

        return ResponseEntity.ok(tokens);
    }


}
