package com.example.demo.jwt;

import com.example.demo.error.ErrorJwtCode;
import com.example.demo.service.jwtservice.UserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();

        if (path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs") || path.startsWith("/swagger-resources")) {
            filterChain.doFilter(request,response);
            return;
        }
        else if (path.contains("/auth/login") || path.contains("/auth/login/register") || path.contains("/auth/login/kakao") ||path.contains("/auth/refresh")) {
            filterChain.doFilter(request,response);
            return;
        }

        // 헤더에서 Token을 따옴
        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        ErrorJwtCode errorCode;
        try {
            if (accessToken != null && jwtTokenProvider.validateToken(response, accessToken)) {
                // Get the username from the access token
                log.info("jwt필터진입");
                String email = jwtTokenProvider.getUserEmailFromAccessToken(accessToken);
                // Load the user details
                UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(email);
                // Create an authentication object
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                // Set the authentication object in the security context
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (MalformedJwtException e) {
            errorCode = ErrorJwtCode.INVALID_TOKEN;
            setResponse(response, errorCode);
            return;
        } catch (ExpiredJwtException e) {
            errorCode = ErrorJwtCode.EXPIRED_AT;
            setResponse(response, errorCode);
            return;
        } catch (UnsupportedJwtException e) {
            errorCode = ErrorJwtCode.UNSUPPORTED_TOKEN;
            setResponse(response, errorCode);
            return;
        } catch (IllegalArgumentException e) {
            errorCode = ErrorJwtCode.EMPTY_TOKEN;
            setResponse(response, errorCode);
            return;
        } catch (SignatureException e) {
            errorCode = ErrorJwtCode.SIGNATURE_MISMATCH;
            setResponse(response, errorCode);
            return;
        } catch (RuntimeException e) {
            errorCode = ErrorJwtCode.JWT_COMPLEX_ERROR;
            setResponse(response, errorCode);
            return;
        }

        filterChain.doFilter(request, response);
    }
    private void setResponse(HttpServletResponse response, ErrorJwtCode errorCode) throws IOException {
        JSONObject json = new JSONObject();
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        json.put("code", errorCode.getCode());
        json.put("message", errorCode.getMessage());

        response.getWriter().print(json);
        response.getWriter().flush();
    }
}
