package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:3000"})
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("http://localhost:8080","http://localhost:3000")
                .exposedHeaders("Authorization","RefreshToken","exception")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(false);

    }
}