package com.example.demo.jwt;

import com.example.demo.error.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-expriation}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-expriation}")
    private long refreshTokenExpiration;

    //토큰 생성 메서드 구현
    //AT 구현

    public String generateAccessToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpiration);
        log.info(String.valueOf(now.getTime()));
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String generateRefreshToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpiration);

        return Jwts.builder()
                .setSubject(email)
                .claim("type", "refresh")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    //헤더에 있는 AT토큰 가져오기
    @Transactional
    public String resolveAccessToken(HttpServletRequest request) {
        if (request.getHeader("Authorization") != null )
            return request.getHeader("Authorization").substring(7);
        return null;
    }
    @Transactional
    public String resolveRefreshToken(HttpServletRequest request){
        if (request.getHeader("RefreshToken") != null)
            return request.getHeader("RefreshToken").substring(7);
        return null;
    }
   /* public String getUserEmailFromAccessToken(String token) {
        String temp = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        return temp;
    }*/
   public String getUserEmailFromAccessToken(String token) {
       try {
           String temp = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
           return temp;
       } catch (NullPointerException e) {
           // JWT 토큰 형식이 잘못되었거나 | 서명이 유효하지 않은 경우
           // 적절한 로그를 출력하거나 처리할 수 있습니다.
           System.out.println("이메일을 찾을 수 없어요: " + e.getMessage());
           return null;
       }
   }

    public String getUsernameFromRefreshToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public boolean validateToken(HttpServletResponse response, String token){
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            response.addHeader("exception", String.valueOf(ErrorCode.EXPIRED_TOKEN.getCode()));
        } catch (SignatureException e) {
            response.addHeader("exception", String.valueOf(ErrorCode.INVALID_TOKEN.getCode()));
        } catch (UnsupportedJwtException e) {
            response.addHeader("exception", String.valueOf(ErrorCode.INVALID_TOKEN.getCode()));
        } catch (IllegalArgumentException e) {
            response.addHeader("exception", String.valueOf(ErrorCode.NON_LOGIN.getCode()));
        }
        return false;
    }

//    public boolean isRefreshToken(String token) {
//        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
//        return claims.get("type") != null && claims.get("type").equals("refresh");
//    }

    public String refreshAccessToken(String token, HttpServletResponse response) throws UnsupportedEncodingException {

        if (!validateToken(response, token)) {
            throw new RuntimeException("Invalid refresh token");
        }
        String userEmail = getUsernameFromRefreshToken(token);
        return userEmail;
    }
}
