package com.example.demo.jwt;

import com.example.demo.enumCustom.UserRole;
import com.example.demo.error.ErrorCode;
import com.example.demo.error.exception.JwtException;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
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
    public Claims getClaimsByRequest(HttpServletRequest request) {
        return getClaimFromAccessToken(resolveAccessToken(request));
    }

    public String getUserEmailFromAccessToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }
    public Claims getClaimFromAccessToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateAccessToken(String email, UserRole userRole) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpiration);

        Claims claims = Jwts.claims()
                .setSubject(email)
                .setExpiration(expiryDate);

        //추가 정보
        claims.put("ROLE", userRole);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String generateRefreshToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpiration);

        Claims claims = Jwts.claims()
                .setSubject(email)
                .setExpiration(expiryDate);
        claims.put("type", "refresh");

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
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

    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            throw new JwtException(ErrorCode.INVALID_TOKEN.getMessage(), ErrorCode.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new JwtException(ErrorCode.EXPIRED_AT.getMessage(), ErrorCode.EXPIRED_AT);
        } catch (UnsupportedJwtException e) {
            throw new JwtException(ErrorCode.INVALID_TOKEN.getMessage(), ErrorCode.INVALID_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new JwtException(ErrorCode.EMPTY_TOKEN.getMessage(), ErrorCode.EMPTY_TOKEN);
        } catch (SignatureException e) {
            throw new JwtException(ErrorCode.INVALID_TOKEN.getMessage(), ErrorCode.INVALID_TOKEN);
        }
    }

    public String refreshAccessToken(String token) {

        validateToken(token);

        return getUserEmailFromAccessToken(token);
    }
}
