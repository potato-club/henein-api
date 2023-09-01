package com.example.demo.service;

import com.example.demo.dto.board.BoardListResponseDto;
import com.example.demo.dto.userchar.*;
import com.example.demo.dto.login.BasicLoginRequestDto;
import com.example.demo.dto.login.KakaoOAuth2User;

import com.example.demo.dto.user.UserInfoResponseDto;
import com.example.demo.dto.user.UserNicknameChange;
import com.example.demo.entity.*;
import com.example.demo.enumCustom.UserRole;
import com.example.demo.error.ErrorCode;
import com.example.demo.error.exception.BadRequestException;
import com.example.demo.error.exception.DuplicateException;
import com.example.demo.error.exception.UnAuthorizedException;
import com.example.demo.error.exception.NotFoundException;
import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.jwt.KakaoOAuth2AccessTokenResponse;
import com.example.demo.jwt.KakaoOAuth2Client;
import com.example.demo.repository.UserCharRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.jwtservice.KakaoOAuth2UserDetailsServcie;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.*;
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
    private final PasswordEncoder passwordEncoder;
    private final WebClient infoWebClient;
    private final WebClient cubeWebClient;
    private final RedisService redisService;
    private final JPAQueryFactory jpaQueryFactory;


//=================필터사용
    @Transactional
    public UserEntity fetchUserEntityByHttpRequest(HttpServletRequest request){
        try {
            String AT = jwtTokenProvider.resolveAccessToken(request);

            String userEmail = jwtTokenProvider.getUserEmailFromAccessToken(AT); // 정보 가져옴

            return userRepository.findByUserEmail(userEmail).
                    orElseThrow(() -> new UnAuthorizedException(ErrorCode.INVALID_ACCESS.getMessage(),ErrorCode.INVALID_ACCESS));
        }catch (NullPointerException e){
            throw new NullPointerException(e.getMessage());
        }
    }
    @Transactional
    public ResponseEntity<?> refreshAT(HttpServletRequest request,HttpServletResponse response) {
        //bearer 지우기
        String RTHeader = jwtTokenProvider.resolveRefreshToken(request);

        // rt 넣어서 검증하고 유저이름 가져오기 /
        String userEmail = jwtTokenProvider.refreshAccessToken(RTHeader ,response);
        UserEntity userEntity = userRepository.findByUserEmail(userEmail).orElseThrow(()->{throw new RuntimeException();});
        //db에 있는 토큰값과 넘어온 토큰이 같은지
        if (!userEntity.getRefreshToken().equals(RTHeader)){
            throw new UnAuthorizedException(ErrorCode.EXPIRED_RT.getMessage(),ErrorCode.EXPIRED_RT);
        }
        String newAccessToken = jwtTokenProvider.generateAccessToken(userEmail);

        // Set the new access token in the HTTP response headers
        response.setHeader("Authorization", "Bearer " + newAccessToken);

        // Optionally, return the new access token in the response body as well
        return ResponseEntity.ok("good");
    }
    //===============마이페이지 관련
    @Transactional
    public UserInfoResponseDto userInfo(HttpServletRequest request){
        UserEntity userEntity = fetchUserEntityByHttpRequest(request);
        return new UserInfoResponseDto(userEntity);
    }
    @Transactional
    public String userNicknameChange(HttpServletRequest request, UserNicknameChange userNickname) {
        UserEntity userEntity = fetchUserEntityByHttpRequest(request);
        userEntity.Update(userNickname);
        userRepository.save(userEntity);
        return "유저 이름 설정 완료";
    }
    @Transactional
    public void pickCharacter(Long id, HttpServletRequest request) {
        UserEntity userEntity = fetchUserEntityByHttpRequest(request);
        UserCharEntity oldCharEntity = userCharRepository.findByUserEntityAndPickByUser(userEntity, true);

        if (oldCharEntity == null){
            UserCharEntity newCharEntity = userCharRepository.findByUserEntityAndId(userEntity, id);
            newCharEntity.pickThisCharacter();
            return;
        }

        oldCharEntity.unPickThisCharacter();

        UserCharEntity newCharEntity = userCharRepository.findByUserEntityAndId(userEntity, id);
        newCharEntity.pickThisCharacter();

    }
    //캐릭터 관련
    @Transactional
    public List<UserCharacter> getAllUserCharacterInfo(HttpServletRequest request) {
        UserEntity userEntity = fetchUserEntityByHttpRequest(request);

        List<UserCharEntity> resultList = userCharRepository.findAllByUserEntity(userEntity);

        return resultList.stream().map(UserCharacter::new).collect(Collectors.toList());
    }
    private static final int character_limit = 100;
    //인증 받아오기
    public String requestToNexon(HttpServletRequest request,UserMapleApi userMapleApi){
        UserEntity userEntity = fetchUserEntityByHttpRequest(request);

         this.cubeWebClient.post()
                .body(BodyInserters.fromValue(userMapleApi))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .flatMap(result -> {
                    if (100 > userCharRepository.countByUserEntity(userEntity)) {
                        List<UserCharEntity> userCharEntityList = new ArrayList<>();
                        for (int i = 0; i < result.size(); i++) {
                            userCharEntityList.add(new UserCharEntity(userEntity, result.get(i)));
                        }
                        userCharRepository.saveAll(userCharEntityList);
                    } else return Mono.error(()->new RuntimeException("over of max character"));
                    return Mono.just(result);
                });
         return "update finish";
    }
    @Transactional
    public String requestUpdateToNode(String userCharName){
        UserCharEntity userCharEntity = userCharRepository.findByNickName(userCharName)
                .orElseThrow(()->{throw new NotFoundException(ErrorCode.NULL_VALUE.getMessage(),ErrorCode.NULL_VALUE);});
        //요청 보내기전에 1시간 시간 제한 걸어야함 레디스 유효시간 1시간임
        if (redisService.checkRedis(userCharName)) {
            throw new BadRequestException(ErrorCode.NULL_VALUE.getMessage(),ErrorCode.NULL_VALUE); // 몇분 남았는지도 알려줘야함
        }
        Map<String, String> callback = new HashMap<>();
        callback.put("callback", "https://henesysback.shop/userinfo/character/info");
        //노드로 요청
         FirstResponseNodeDto result = this.infoWebClient.put()
                .uri(userCharName)
                .body(BodyInserters.fromValue(callback))
                .retrieve()
                .bodyToMono(FirstResponseNodeDto.class)
                .block();
        //거절시 null로 오나? 확인해야함
        if (result == null){
            throw new RuntimeException();
        }
        return redisService.setWorkStatus(userCharName);
    }
    @Transactional
    public String responseToRedisAndUpdate(NodeConnection nodeConnection){
        if (!userCharRepository.existsByNickName(nodeConnection.getDetailCharacter().getNickname())){
            throw new NotFoundException(ErrorCode.NULL_VALUE.getMessage(),ErrorCode.NULL_VALUE);
        }
        UserCharEntity userCharEntity = userCharRepository.findByNickName(nodeConnection.getDetailCharacter().getNickname())
                .orElseThrow(()->{throw new NotFoundException(ErrorCode.NOT_FOUND.getMessage(),ErrorCode.NOT_FOUND);});

        userCharEntity.update(nodeConnection.getDetailCharacter());

        return redisService.updateWork(nodeConnection.getDetailCharacter().getNickname());
    }

    //============내 활동관련 =======================//

    @Transactional
    public List<BoardListResponseDto> getMyBoardList(HttpServletRequest request) {
        UserEntity userEntity = fetchUserEntityByHttpRequest(request);

        QBoardEntity qBoardEntity= QBoardEntity.boardEntity;

        List<BoardEntity> boardEntityList = jpaQueryFactory
                .selectFrom(qBoardEntity)
                .where(qBoardEntity.userEntity.eq(userEntity))
                .orderBy(qBoardEntity.id.desc())
                .fetch();

        return boardEntityList.stream().map(BoardListResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public List<BoardListResponseDto> getMyBoardsWithCommentList(HttpServletRequest request) {
        UserEntity userEntity = fetchUserEntityByHttpRequest(request);

        QCommentEntity qCommentEntity = QCommentEntity.commentEntity;

        List<BoardEntity> boardEntityList = jpaQueryFactory
                .select(qCommentEntity.boardEntity)
                .from(qCommentEntity)
                .where(qCommentEntity.userEmail.eq(userEntity.getUserEmail()))
                .orderBy(qCommentEntity.boardEntity.id.desc())
                .fetch();

        return boardEntityList.stream().map(BoardListResponseDto::new).collect(Collectors.toList());
    }


    //==================로그인 관련
    @Transactional
    public ResponseEntity<String> basicLogin(BasicLoginRequestDto basicLoginRequestDto, HttpServletResponse response){
        UserEntity userEntity = userRepository.findByUserEmail(basicLoginRequestDto.getUserEmail()).orElseThrow(()->{
            throw new UnAuthorizedException(ErrorCode.INVALID_ACCESS.getMessage(),ErrorCode.INVALID_ACCESS);});

        if ( !passwordEncoder.matches(basicLoginRequestDto.getPassword(),userEntity.getPassword()) ) {
            throw new UnAuthorizedException(ErrorCode.INVALID_ACCESS.getMessage(),ErrorCode.INVALID_ACCESS);
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
            throw new DuplicateException(ErrorCode.DUPLICATE_EMAIL.getMessage(),ErrorCode.DUPLICATE_EMAIL);
        }

        //토큰 발급
        String accessToken = jwtTokenProvider.generateAccessToken(basicLoginRequestDto.getUserEmail());
        String refreshToken = jwtTokenProvider.generateRefreshToken(basicLoginRequestDto.getUserEmail());

        //디비 저장
        UserEntity userEntity = new UserEntity().builder()
                .userRole(UserRole.USER)
                .uid(String.valueOf(UUID.randomUUID()))
                .userName("ㅇㅇ")
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
    public ResponseEntity<?> kakaoLogin(String code, HttpServletResponse response) {

        KakaoOAuth2AccessTokenResponse tokenResponse = kakaoOAuth2Client.getAccessToken(code);
        // 카카오 사용자 정보를 가져옵니다.
        KakaoOAuth2User kakaoOAuth2User = kakaoOAuth2Client.getUserProfile(tokenResponse.getAccessToken());

        // 사용자 정보를 기반으로 우리 시스템에 인증을 수행합니다.
        Authentication authentication = new UsernamePasswordAuthenticationToken(kakaoOAuth2User, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // JWT 토큰을 발급합니다.
        String email = kakaoOAuth2User.getKakao_account().getEmail();
        log.info("JWT 토큰을 발급합니다 Controller: "+email);
        String accessToken = jwtTokenProvider.generateAccessToken(email);
        String refreshToken = jwtTokenProvider.generateRefreshToken(email);
        Map<String, String> tokens =new HashMap<>();
        if (!userRepository.existsByUserEmail(email)){
            tokens.put("status","신규 유저입니다.");
        }
        // 로그인한 사용자의 정보를 저장합니다.
        kakaoOAuth2UserDetailsServcie.loadUserByKakaoOAuth2User(email, refreshToken);

        //클라이언트에게 리턴해주기
        response.setHeader("Authorization","Bearer " + accessToken);
        response.setHeader("RefreshToken","Bearer " + refreshToken);

        return ResponseEntity.ok(tokens);
    }


}
