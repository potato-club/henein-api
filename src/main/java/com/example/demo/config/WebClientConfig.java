package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient APIClient(WebClient.Builder builder) {
        return builder
                .baseUrl("https://heneinbackapi.shop/") // 기본 URL 설정
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8") // 기본 헤더 설정
                .build();
    }
}
