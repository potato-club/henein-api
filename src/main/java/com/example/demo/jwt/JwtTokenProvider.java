package com.example.demo.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Date;

@Component
@Slf4j
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
        if(request.getHeader("Authorization") != null )
            return request.getHeader("Authorization").substring(7);
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
       } catch (MalformedJwtException | SignatureException e) {
           // JWT 토큰 형식이 잘못되었거나 | 서명이 유효하지 않은 경우
           // 적절한 로그를 출력하거나 처리할 수 있습니다.
           System.out.println("Invalid JWT token: " + e.getMessage());
       } catch (ExpiredJwtException e) {
           // JWT 토큰이 만료된 경우
           System.out.println("Expired JWT token: " + e.getMessage());
       } catch (UnsupportedJwtException e) {
           // 지원되지 않는 JWT 토큰인 경우
           System.out.println("Unsupported JWT token: " + e.getMessage());
       } catch (IllegalArgumentException e) {
           // JWT 토큰이 비어있거나 null인 경우
           System.out.println("Illegal JWT token: " + e.getMessage());
       }

       return null;
   }

    public String getUsernameFromRefreshToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public boolean validateAccessToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("FUCKYOU" + e.getMessage());
        }
    }

    public boolean validateRefreshToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Invalid refresh token");
        }
    }

    public boolean isRefreshToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        return claims.get("type") != null && claims.get("type").equals("refresh");
    }

    public String refreshAccessToken(String token) {
        if (!isRefreshToken(token)) {
            throw new RuntimeException("Invalid refresh token");
        }

        if (!validateRefreshToken(token)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String username = getUsernameFromRefreshToken(token);
        return generateAccessToken(username);
    }
}
