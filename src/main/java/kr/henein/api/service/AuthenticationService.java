package kr.henein.api.service;

import kr.henein.api.config.WebClientConfig;
import kr.henein.api.dto.captcha.CaptchaRequestDto;
import kr.henein.api.dto.captcha.CaptchaResponseDto;
import kr.henein.api.dto.login.BasicLoginRequestDto;
import kr.henein.api.dto.login.KakaoOAuth2User;
import kr.henein.api.entity.UserEntity;
import kr.henein.api.enumCustom.UserRole;
import kr.henein.api.error.ErrorCode;
import kr.henein.api.error.exception.BadRequestException;
import kr.henein.api.error.exception.UnAuthorizedException;
import kr.henein.api.jwt.JwtTokenProvider;
import kr.henein.api.jwt.KakaoOAuth2AccessTokenResponse;
import kr.henein.api.jwt.KakaoOAuth2Client;
import kr.henein.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final KakaoOAuth2Client kakaoOAuth2Client;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;

    @Value("${google.recaptcha.key.site-key}")
    private String siteKey;
    @Value("${google.recaptcha.key.secret-key}")
    private String secretKey;
    @Value("${google.recaptcha.key.url}")
    private String url;

    //==================로그인 관련

    @Transactional
    public ResponseEntity<?> refreshAT(HttpServletRequest request,HttpServletResponse response) {
        //bearer 지우기
        String RTHeader = jwtTokenProvider.resolveRefreshToken(request);

        // rt 넣어서 검증하고 유저이름 가져오기
        String userEmail = jwtTokenProvider.refreshAccessToken(RTHeader);
        UserEntity userEntity = userRepository.findByUserEmail(userEmail).orElseThrow(()->{throw new UnAuthorizedException(ErrorCode.NOT_FOUND.getMessage(), ErrorCode.NOT_FOUND);});
        //db에 있는 토큰값과 넘어온 토큰이 같은지
        if (!userEntity.getRefreshToken().equals(RTHeader)){
            throw new UnAuthorizedException(ErrorCode.EXPIRED_RT.getMessage(),ErrorCode.EXPIRED_RT);
        }
        String newAccessToken = jwtTokenProvider.generateAccessToken(userEmail, userEntity.getUserRole());

        // Set the new access token in the HTTP response headers
        response.setHeader("Authorization", "Bearer " + newAccessToken);

        // Optionally, return the new access token in the response body as well
        return ResponseEntity.ok("good");
    }

    @Transactional
    public ResponseEntity<String> basicLogin(BasicLoginRequestDto basicLoginRequestDto, HttpServletResponse response){
        UserEntity userEntity = userRepository.findByUserEmail(basicLoginRequestDto.getUserEmail()).orElseThrow(()->{
            throw new UnAuthorizedException("이메일을 확인하세요", ErrorCode.INVALID_ACCESS);});

        if ( !passwordEncoder.matches(basicLoginRequestDto.getPassword(),userEntity.getPassword()) ) {
            throw new UnAuthorizedException("비밀번호가 틀렸습니다.",ErrorCode.INVALID_ACCESS);
        }

        String AT = jwtTokenProvider.generateAccessToken(userEntity.getUserEmail(), userEntity.getUserRole());
        String RT = jwtTokenProvider.generateRefreshToken(userEntity.getUserEmail());
        userEntity.setRefreshToken(RT);

        response.setHeader("Authorization","Bearer " + AT);
        response.setHeader("RefreshToken","Bearer "+ RT);
        return ResponseEntity.ok("로그인 성공");
    }

    @Transactional
    public ResponseEntity<String> basicSignUp(BasicLoginRequestDto basicLoginRequestDto, HttpServletRequest request, HttpServletResponse response){

        String requestAT = jwtTokenProvider.resolveAccessToken(request);
        if ( !redisService.verifySignUpRequest(basicLoginRequestDto.getUserEmail(), requestAT) ) {
            throw new UnAuthorizedException("Do not match email with AT", ErrorCode.JWT_COMPLEX_ERROR);
        }
        String AT = jwtTokenProvider.generateAccessToken(basicLoginRequestDto.getUserEmail(), UserRole.USER);
        String RT = jwtTokenProvider.generateRefreshToken(basicLoginRequestDto.getUserEmail());

        String uid = UUID.randomUUID().toString();

        UserEntity userEntity = UserEntity.builder()
                .userRole(UserRole.USER)
                .userName(uid)
                .refreshToken(RT)
                .userEmail(basicLoginRequestDto.getUserEmail())
                .isAnonymous(true)
                .uid(uid)
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

        String email = kakaoOAuth2User.getKakao_account().getEmail();
        String RT = jwtTokenProvider.generateRefreshToken(email);

        UserEntity userEntity = userRepository.findByUserEmail(email)
                .orElseGet(() ->new UserEntity(email));

        //신규회원이면
        Map<String, String> tokens = new HashMap<>();
        if (userEntity.getRefreshToken()==null) {
            tokens.put("status","신규 유저입니다.");
            userEntity.setRefreshToken(RT);
            userRepository.save(userEntity);
        } else {
            userEntity.setRefreshToken(RT);
        }

        String AT = jwtTokenProvider.generateAccessToken(email, userEntity.getUserRole());


        response.setHeader("Authorization","Bearer " + AT);
        response.setHeader("RefreshToken","Bearer " + RT);

        return ResponseEntity.ok(tokens);
    }

    public ResponseEntity<?> validateRecaptcha (String captchaValue) {

        String urI = "?secret="+secretKey+ "&response="+captchaValue;
       return WebClient.builder()
               .baseUrl(url)
               .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE) // 기본 헤더 설정
               .filter(logRequest())
               .build()
               .post()
               .uri(uriBuilder -> uriBuilder
                       .queryParam("secret", secretKey)
                       .queryParam("response", captchaValue)
                       .build())
               .retrieve()
               .bodyToMono(CaptchaResponseDto.class)
                .map(response -> {
                    if( response.isSuccess() ){
                        return ResponseEntity.ok().build();
                    } else {
                        String message = response.getErrorList().stream()
                                .map(Objects::toString)
                                .collect(Collectors.joining(", "));
                        //성공 실패시 response의 error 변수를 throw하고 싶다.
                        throw new BadRequestException(ErrorCode.CAPTCHA_FAILED.getMessage()+message, ErrorCode.CAPTCHA_FAILED);
                    }
                })
                .block();
    }
    private ExchangeFilterFunction logRequest() {
        Logger logger = LoggerFactory.getLogger(WebClientConfig.class);
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            if (logger.isDebugEnabled()) {
                StringBuilder sb = new StringBuilder("Request: \n");
                // 요청 메서드와 URL 출력
                sb.append(clientRequest.method().name())
                        .append(" ")
                        .append(clientRequest.url());

                // 헤더 출력
                clientRequest.headers().forEach((name, values) -> values.forEach(value -> sb.append("\n").append(name).append(":").append(value)));

                // 요청 바디가 있다면 출력 (단순화를 위해 생략될 수 있음)
                clientRequest.body();
                // clientRequest.body()를 사용하여 요청 바디의 내용을 로깅할 수 있습니다.
                // 이는 종종 추가적인 작업이 필요할 수 있습니다.

                logger.debug(sb.toString());
            }
            return Mono.just(clientRequest);
        });
    }
}
