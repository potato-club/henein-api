package com.example.demo.jwt;

import com.example.demo.error.ErrorCode;
import com.example.demo.error.ErrorJwtCode;
import com.example.demo.error.exception.JwtException;
import io.jsonwebtoken.*;
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

    public String fetchUserEmailByHttpRequest(HttpServletRequest request){
        try {
            String AT = resolveAccessToken(request);

            return getUserEmailFromAccessToken(AT);
        }catch (NullPointerException e){
            throw new NullPointerException(e.getMessage());
        }
    }
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
        if (request.getHeader("Authorization") != null && !request.getHeader("Authorization").trim().isEmpty() )
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
       String temp = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
       return temp;
   }

    public String getUsernameFromRefreshToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            throw e;
        } catch (ExpiredJwtException e) {
            throw e;
        } catch (UnsupportedJwtException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (SignatureException e) {
            throw e;
        }
    }
    public boolean validateRefreshToken(String token){
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            throw new JwtException(ErrorCode.INVALID_TOKEN.getMessage(), ErrorCode.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new JwtException(ErrorCode.EXPIRED_RT.getMessage(), ErrorCode.EXPIRED_RT);
        } catch (UnsupportedJwtException e) {
            throw new JwtException(ErrorCode.INVALID_TOKEN.getMessage(), ErrorCode.INVALID_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new JwtException(ErrorCode.EMPTY_TOKEN.getMessage(), ErrorCode.EMPTY_TOKEN);
        } catch (io.jsonwebtoken.SignatureException e) {
            throw new JwtException(ErrorCode.INVALID_TOKEN.getMessage(), ErrorCode.INVALID_TOKEN);
        }
    }

    public String refreshAccessToken(String token) {

        validateRefreshToken(token);

        String userEmail = getUsernameFromRefreshToken(token);
        return userEmail;
    }
}
