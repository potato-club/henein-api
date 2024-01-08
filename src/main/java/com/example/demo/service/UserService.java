package com.example.demo.service;

import com.example.demo.dto.board.BoardListResponseDto;
import com.example.demo.dto.user.UserDetailInfoResponseDto;
import com.example.demo.dto.user.UserInfoChange;
import com.example.demo.dto.user.UserInfoResponseDto;
import com.example.demo.dto.userchar.*;
import com.example.demo.dto.login.BasicLoginRequestDto;
import com.example.demo.dto.login.KakaoOAuth2User;
import com.example.demo.entity.*;
import com.example.demo.enumCustom.S3EntityType;
import com.example.demo.enumCustom.UserRole;
import com.example.demo.error.ErrorCode;
import com.example.demo.error.exception.BadRequestException;
import com.example.demo.error.exception.DuplicateException;
import com.example.demo.error.exception.UnAuthorizedException;
import com.example.demo.error.exception.NotFoundException;
import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.jwt.KakaoOAuth2AccessTokenResponse;
import com.example.demo.jwt.KakaoOAuth2Client;
import com.example.demo.repository.S3FileRespository;
import com.example.demo.repository.UserCharRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.jwtservice.KakaoOAuth2UserDetailsServcie;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;



@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final S3FileRespository s3FileRepository;
    private final S3Service s3Service;
    private final UserCharRepository userCharRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoOAuth2UserDetailsServcie kakaoOAuth2UserDetailsServcie;
    private final KakaoOAuth2Client kakaoOAuth2Client;
    private final PasswordEncoder passwordEncoder;
    private final WebClient infoWebClient;
    private final WebClient cubeWebClient;
    private final RedisService redisService;
    private final JPAQueryFactory jpaQueryFactory;

    @Value("${apiKey}")
    private String apiKey;

//=================필터사용

    @Transactional
    public ResponseEntity<?> refreshAT(HttpServletRequest request,HttpServletResponse response) {
        //bearer 지우기
        String RTHeader = jwtTokenProvider.resolveRefreshToken(request);

        // rt 넣어서 검증하고 유저이름 가져오기
        String userEmail = jwtTokenProvider.refreshAccessToken(RTHeader);
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
    //===============마이페이지 관련===================

    public UserInfoResponseDto userInfo(HttpServletRequest request){
        String userEmail = jwtTokenProvider.fetchUserEmailByHttpRequest(request);
        UserEntity userEntity = userRepository.findByUserEmail(userEmail).orElseThrow(()->{throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION.getMessage(), ErrorCode.NOT_FOUND_EXCEPTION);});


        UserCharEntity userCharEntity = userCharRepository.findByUserEntityAndPickByUser(userEntity,true);

        List<S3File> s3File = s3FileRepository.findAllByS3EntityTypeAndTypeId(S3EntityType.USER,userEntity.getId());

        if ( s3File.size() == 0 && userCharEntity == null ) {
            return new UserInfoResponseDto(userEntity,null,null);
        }
        else if (userCharEntity == null) {
            return new UserInfoResponseDto(userEntity,null,s3File.get(0).getFileUrl());
        }
        else if (s3File.size() == 0) {
            return new UserInfoResponseDto(userEntity,userCharEntity.getNickName(),null);
        }

        return new UserInfoResponseDto(userEntity,userCharEntity.getNickName(),s3File.get(0).getFileUrl());
    }

    public UserDetailInfoResponseDto userDetailInfo(HttpServletRequest request) {
        String userEmail = jwtTokenProvider.fetchUserEmailByHttpRequest(request);
        UserEntity userEntity = userRepository.findByUserEmail(userEmail).orElseThrow(()->{throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION.getMessage(), ErrorCode.NOT_FOUND_EXCEPTION);});


        QBoardEntity qBoardEntity= QBoardEntity.boardEntity;
        long boardCount = jpaQueryFactory
                .selectFrom(qBoardEntity)
                .where(qBoardEntity.userEntity.eq(userEntity))
                .fetchCount();

        QCommentEntity qCommentEntity = QCommentEntity.commentEntity;
        long commentCount = jpaQueryFactory
                .selectFrom(qCommentEntity)
                .where(qCommentEntity.userEmail.eq(userEntity.getUserEmail()))
                .fetchCount();

        List<S3File> s3File = s3FileRepository.findAllByS3EntityTypeAndTypeId(S3EntityType.USER,userEntity.getId());

        if (s3File.size() == 0) {
            return new UserDetailInfoResponseDto(userEntity,null,boardCount,commentCount);
        }
        return new UserDetailInfoResponseDto(userEntity,s3File.get(0).getFileUrl(),boardCount,commentCount);
    }
    @Transactional
    public String userUpdate(UserInfoChange userInfoChange, HttpServletRequest request) throws IOException {
        String userEmail = jwtTokenProvider.fetchUserEmailByHttpRequest(request);
        UserEntity userEntity = userRepository.findByUserEmail(userEmail).orElseThrow(()->{throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION.getMessage(), ErrorCode.NOT_FOUND_EXCEPTION);});

        if (!userInfoChange.getUserName().trim().isEmpty()) {
            userEntity.Update(userInfoChange.getUserName());
        }
        if (!(userInfoChange.getImage() == null || userInfoChange.getImage().isEmpty())) {
            s3Service.uploadImageUserPicture(userInfoChange.getImage(), userEntity.getId());
        }

        return "200ok";
    }

    @Transactional
    public void pickCharacter(Long id, HttpServletRequest request) {
        String userEmail = jwtTokenProvider.fetchUserEmailByHttpRequest(request);
        UserEntity userEntity = userRepository.findByUserEmail(userEmail).orElseThrow(()->{throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION.getMessage(), ErrorCode.NOT_FOUND_EXCEPTION);});

        UserCharEntity oldCharEntity = userCharRepository.findByUserEntityAndPickByUser(userEntity, true);

        if (oldCharEntity == null){
            UserCharEntity newCharEntity = userCharRepository.findByUserEntityAndId(userEntity, id);
            newCharEntity.pickThisCharacter();
            return;
        }
        else if (oldCharEntity.getId() == id){
            oldCharEntity.unPickThisCharacter();
            return;
        }

        UserCharEntity newCharEntity = userCharRepository.findByUserEntityAndId(userEntity, id);

        oldCharEntity.unPickThisCharacter();
        newCharEntity.pickThisCharacter();

    }
    //캐릭터 관련
    @Transactional
    public List<UserCharacter> getAllUserCharacterInfo(HttpServletRequest request) {
        String userEmail= jwtTokenProvider.fetchUserEmailByHttpRequest(request);
        UserEntity userEntity = userRepository.findByUserEmail(userEmail).orElseThrow(()->{throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION.getMessage(), ErrorCode.NOT_FOUND_EXCEPTION);});

        List<UserCharEntity> resultList = userCharRepository.findAllByUserEntity(userEntity);
        log.info(resultList.get(0).getAvatar());
        return resultList.stream().map(UserCharacter::new).collect(Collectors.toList());
    }

    //인증 받아오기
    public String requestToNexon(HttpServletRequest request,UserMapleApi userMapleApi){
        //날짜가 오늘일시 에러.
        if (userMapleApi.getRecentDay().equals(LocalDate.now())) {
            throw new BadRequestException("Today's date cannot be requested", ErrorCode.BAD_REQUEST);
        }
        else if (ChronoUnit.DAYS.between(userMapleApi.getRecentDay(),userMapleApi.getPastDay()) > 61){
            throw new BadRequestException("Cannot request for more than 2 months", ErrorCode.BAD_REQUEST);
        }
        //날짜 비교해서 2달 이상이면 에러
        String userEmail = jwtTokenProvider.fetchUserEmailByHttpRequest(request);
        UserEntity userEntity = userRepository.findByUserEmail(userEmail).orElseThrow(()->{throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION.getMessage(), ErrorCode.NOT_FOUND_EXCEPTION);});

        String api = "cube?key="+apiKey;

         this.cubeWebClient.post()
                 .uri(api)
                .body(BodyInserters.fromValue(userMapleApi))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .flatMap(result -> {

                    List<UserCharEntity> userCharEntityList = new ArrayList<>();

                    for (int i = 0; i < result.size(); i++) {
                        if (!userCharRepository.existsByNickName(result.get(i))) {
                            userCharEntityList.add(new UserCharEntity(userEntity, result.get(i)));
                        }
                    }
                    if (!userCharEntityList.isEmpty()) {
                        userCharRepository.saveAll(userCharEntityList);
                    }
                    return null;
                })
                 .subscribe();
         return "Please wait about 30 seconds and try /userinfo/all";
    }

    @Transactional
    public String requestUpdateToNode(String userCharName){
        //저장된 캐릭인지
        if (!userCharRepository.existsByNickName(userCharName)) {
            throw new NotFoundException(ErrorCode.NULL_VALUE.getMessage(),ErrorCode.NULL_VALUE);
        }
        //요청 보내기전에 1시간 시간 제한 걸어야함 레디스 유효시간 1시간임
        else if (redisService.checkRedis(userCharName)) {
            throw new BadRequestException("1시간 요청 제한",ErrorCode.NULL_VALUE); // 몇분 남았는지도 알려줘야함
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
        log.info(nodeConnection.getNickname());
        log.info(nodeConnection.getCharacter().getAvatar());
        if (!userCharRepository.existsByNickName(nodeConnection.getCharacter().getNickname())){
            throw new NotFoundException(ErrorCode.NULL_VALUE.getMessage(),ErrorCode.NULL_VALUE);
        }
        UserCharEntity userCharEntity = userCharRepository.findByNickName(nodeConnection.getCharacter().getNickname())
                .orElseThrow(()->{throw new NotFoundException(ErrorCode.NOT_FOUND.getMessage(),ErrorCode.NOT_FOUND);});

        userCharEntity.update(nodeConnection.getCharacter());

        return redisService.updateWork(nodeConnection.getCharacter().getNickname());
    }

    //============내 활동관련 =======================//

    @Transactional
    public List<BoardListResponseDto> getMyBoardList(HttpServletRequest request) {
        String userEmail = jwtTokenProvider.fetchUserEmailByHttpRequest(request);

        QBoardEntity qBoardEntity= QBoardEntity.boardEntity;

        List<BoardEntity> boardEntityList = jpaQueryFactory
                .selectFrom(qBoardEntity)
                .where(qBoardEntity.userEntity.userEmail.eq(userEmail))
                .orderBy(qBoardEntity.id.desc())
                .fetch();

        return boardEntityList.stream().map(BoardListResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public List<BoardListResponseDto> getMyBoardsWithCommentList(HttpServletRequest request) {
        String userEmail = jwtTokenProvider.fetchUserEmailByHttpRequest(request);

        QCommentEntity qCommentEntity = QCommentEntity.commentEntity;

        List<BoardEntity> boardEntityList = jpaQueryFactory
                .select(qCommentEntity.boardEntity)
                .distinct()
                .from(qCommentEntity)
                .where(qCommentEntity.userEmail.eq(userEmail))
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
        System.out.print("hi");

        String accessToken = jwtTokenProvider.generateAccessToken(basicLoginRequestDto.getUserEmail());
        String refreshToken = jwtTokenProvider.generateRefreshToken(basicLoginRequestDto.getUserEmail());
        userEntity.setRefreshToken(refreshToken);
        response.setHeader("Authorization","Bearer " + accessToken);
        response.setHeader("RefreshToken","Bearer "+ refreshToken);
        return ResponseEntity.ok("로그인 성공");
    }

    @Transactional
    public ResponseEntity<String> basicSignUp(BasicLoginRequestDto basicLoginRequestDto, HttpServletRequest request, HttpServletResponse response){
        //이메일 검증 성공했을테니 디비에서 찾아야함.
        String requestAT = jwtTokenProvider.resolveAccessToken(request);
        if ( !redisService.verifySignUpRequest(basicLoginRequestDto.getUserEmail(), requestAT) ) {
            throw new UnAuthorizedException("Do not match email with AT", ErrorCode.JWT_COMPLEX_ERROR);
        }
        String AT = jwtTokenProvider.generateAccessToken(basicLoginRequestDto.getUserEmail());
        String RT = jwtTokenProvider.generateRefreshToken(basicLoginRequestDto.getUserEmail());

        UserEntity userEntity = UserEntity.builder()
                .userRole(UserRole.USER)
                .userName("ㅇㅇ")
                .refreshToken(RT)
                .userEmail(basicLoginRequestDto.getUserEmail())
                .uid(UUID.randomUUID().toString())
                .password(passwordEncoder.encode(basicLoginRequestDto.getPassword()))
                .build();
        userRepository.save(userEntity);

        response.setHeader("Authorization","Bearer " + AT);
        response.setHeader("RefreshToken","Bearer "+ RT);


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
