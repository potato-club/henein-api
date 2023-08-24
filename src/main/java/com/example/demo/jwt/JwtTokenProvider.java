package com.example.demo.jwt;

import com.example.demo.error.ErrorCode;
import com.example.demo.error.exception.JwtException;
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


    public String generateAccessToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpiration);
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

   public String getUserEmailFromAccessToken(String token) {
       try {
           String temp = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
           return temp;
       } catch (ExpiredJwtException e) {
           throw new JwtException("토큰이 만료되었습니다.",101);
       } catch (NullPointerException e) {
           return null;
       }
   }

    public String getUsernameFromRefreshToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public boolean validateToken(HttpServletResponse response, String token){
        try {
            log.info("토큰 인증으로 들어옴");
            log.info(token);
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new JwtException("토큰이 만료되었습니다.",101);
        } catch (SignatureException e) {
            response.addHeader("exception", String.valueOf(ErrorCode.INVALID_TOKEN.getCode()));
        } catch (UnsupportedJwtException e) {
            response.addHeader("exception", String.valueOf(ErrorCode.INVALID_TOKEN.getCode()));
        } catch (IllegalArgumentException e) {
            response.addHeader("exception", String.valueOf(ErrorCode.NON_LOGIN.getCode()));
        }
        return false;
    }
    public boolean validateRefreshToken(HttpServletResponse response, String token){
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            response.addHeader("exception", String.valueOf(ErrorCode.RE_LOGIN.getCode()));
        } catch (SignatureException e) {
            response.addHeader("exception", String.valueOf(ErrorCode.INVALID_TOKEN.getCode()));
        } catch (UnsupportedJwtException e) {
            response.addHeader("exception", String.valueOf(ErrorCode.INVALID_TOKEN.getCode()));
        } catch (IllegalArgumentException e) {
            response.addHeader("exception", String.valueOf(ErrorCode.NON_LOGIN.getCode()));
        }
        return false;
    }

    public String refreshAccessToken(String token, HttpServletResponse response) throws UnsupportedEncodingException {

        if (!validateRefreshToken(response, token)) {
            throw new RuntimeException("Invalid refresh token");
        }
        String userEmail = getUsernameFromRefreshToken(token);
        return userEmail;
    }
}
