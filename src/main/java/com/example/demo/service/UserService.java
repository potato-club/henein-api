package com.example.demo.service;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RefreshTokenRequest;
import com.example.demo.dto.TokenResponse;
import com.example.demo.dto.UserRegisterRequest;
import com.example.demo.entity.UserEntity;
import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public ResponseEntity<?> login(LoginRequest loginRequest, HttpServletResponse response){
        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        log.info(authentication.getName());

        // Generate the access and refresh tokens
        String accessToken = jwtTokenProvider.generateAccessToken(authentication.getName());
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication.getName());

        response.setHeader("Authorization","Bearer "+accessToken);

        // Return the tokens in the response
        return ResponseEntity.ok(new TokenResponse(accessToken, refreshToken));
    }

    @Transactional
    public ResponseEntity<?> refreshAT(String RTHeader,HttpServletResponse response){
        //bearer 지우기
        String RT = RTHeader.substring(7);

        try {
            // Validate the refreshToken and generate a new accessToken
            String newAccessToken = jwtTokenProvider.refreshAccessToken(RTHeader);

            // Set the new access token in the HTTP response headers
            response.setHeader("Authorization", "Bearer " + newAccessToken);

            // Optionally, return the new access token in the response body as well
            return ResponseEntity.ok(newAccessToken);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
    @Transactional
    public String registerUser(UserRegisterRequest userRegisterRequest){
        //이미 있는 이름인지 확인
        if (userRepository.existsByUsername(userRegisterRequest.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }
        // Create a new user and Save
        userRegisterRequest.setPassword(passwordEncoder.encode(userRegisterRequest.getPassword()));
        userRepository.save(userRegisterRequest.toEntity(userRegisterRequest));

        return "저장 완료";

    }
}
