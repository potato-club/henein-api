package com.example.demo.jwt;

import com.example.demo.dto.login.KakaoOAuth2User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class KakaoOAuth2Client {
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    private static final String KAKAO_OAUTH_BASE_URL = "https://kauth.kakao.com";
    private static final String KAKAO_API_BASE_URL = "https://kapi.kakao.com";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public KakaoOAuth2Client() {
        restTemplate = new RestTemplate();
        objectMapper = new ObjectMapper();
    }

    public KakaoOAuth2AccessTokenResponse getAccessToken(String code) {
        String accessTokenUrl = KAKAO_OAUTH_BASE_URL + "/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoClientId);
        params.add("redirect_uri", kakaoRedirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.exchange(accessTokenUrl, HttpMethod.POST, request, String.class);

        KakaoOAuth2AccessTokenResponse tokenResponse;
        try {
            tokenResponse = objectMapper.readValue(response.getBody(), KakaoOAuth2AccessTokenResponse.class);
        } catch (IOException | JsonProcessingException e) {
            throw new RuntimeException("Error parsing Kakao OAuth2 Access Token Response", e);
        }

        return tokenResponse;
    }

    public KakaoOAuth2User getUserProfile(String accessToken) {
        String userProfileUrl = KAKAO_API_BASE_URL + "/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<?> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(userProfileUrl, HttpMethod.POST, request, String.class);

        KakaoOAuth2User kakaoOAuth2User;
        try {
            kakaoOAuth2User = objectMapper.readValue(response.getBody(), KakaoOAuth2User.class);
        } catch (IOException | JsonProcessingException e) {
            throw new RuntimeException("Error parsing Kakao OAuth2 User Profile", e);
        }

        return kakaoOAuth2User;
    }
}
