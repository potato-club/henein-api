package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient cubeWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("https://heneinbackapi.shop/cube") // 기본 URL 설정
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) // 기본 헤더 설정
                .build();
    }
    @Bean
    public WebClient infoWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("https://info.henein.kr/v1/character/") // 기본 URL 설정
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) // 기본 헤더 설정
                .build();
    }
}
