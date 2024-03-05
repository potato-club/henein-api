package kr.henein.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient APIClient(WebClient.Builder builder) {
        return builder
                .baseUrl("https://heneinbackapi.shop") // 기본 URL 설정
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8") // 기본 헤더 설정
                .build();
    }
}
