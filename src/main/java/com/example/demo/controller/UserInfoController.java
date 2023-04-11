package com.example.demo.controller;

import com.example.demo.dto.user.UserInfoResponseDto;
import com.example.demo.service.UserService;
import io.swagger.annotations.Api;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/userinfo")
@RequiredArgsConstructor
@Slf4j
@Api(tags = {"유저 Controller"})
@CrossOrigin(origins = "http://localhost:3000")
public class UserInfoController {
    private final UserService userService;
    @GetMapping
    public UserInfoResponseDto userInfo(HttpServletRequest request){
        return userService.userInfo(request);
    }
}
