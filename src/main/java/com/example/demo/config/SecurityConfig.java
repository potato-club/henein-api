package com.example.demo.config;

import com.example.demo.jwt.JwtAuthenticationFilter;
import com.example.demo.jwt.KakaoOAuth2Client;
import com.example.demo.service.jwtservice.KakaoOAuth2UserDetailsServcie;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final KakaoOAuth2UserDetailsServcie kakaoOAuth2UserDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .csrf().disable()
                .cors()
                .and()
                .formLogin().disable()
                .logout()
                .logoutSuccessUrl("/")
                .and()

                .authorizeRequests()
                // .antMatchers(HttpMethod.POST,"/board/recommend").hasAnyAuthority("USER","ADMIN")
                .antMatchers(HttpMethod.OPTIONS,"/**").permitAll()//userinfo불러올때의 CORS를 해결하기 위해 OPTIONS에 대한 설정함
                .antMatchers("/userinfo/test").permitAll()
                .antMatchers("/auth/login").permitAll()
                .antMatchers("/auth/login/**").permitAll()
                .antMatchers(HttpMethod.GET,"/board/**").permitAll() // /board/**에 대한 GET 요청을 허용합니다.
                .antMatchers("/board/**").authenticated()
                .anyRequest().authenticated()

                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtAuthenticationFilter, BasicAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder().build();
    }

    @Bean
    public KakaoOAuth2Client kakaoOAuth2Client() {
        return new KakaoOAuth2Client();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(kakaoOAuth2UserDetailsService);
    }

}