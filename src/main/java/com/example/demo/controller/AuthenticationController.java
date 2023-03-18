package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RefreshTokenRequest;
import com.example.demo.dto.TokenResponse;
import com.example.demo.dto.UserRegisterRequest;
import com.example.demo.entity.UserEntity;
import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserRepository userRepository;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
       return userService.login(loginRequest, response);
    }
    @GetMapping("/refresh")
    public ResponseEntity<?> refreshAT(@RequestHeader("Authorization") String RTHeader,HttpServletResponse response) {
       return userService.refreshAT(RTHeader, response);
    }
    @PostMapping("/register")
    public String register(@RequestBody UserRegisterRequest userRegisterRequest){
        return userService.registerUser(userRegisterRequest);
    }


}
