package com.example.demo.controller;

import com.example.demo.dto.user.UserInfoResponseDto;
import com.example.demo.dto.user.UserNicknameChange;
import com.example.demo.service.UserService;
import io.swagger.annotations.Api;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

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
    @Tag(name = "신규유저 이름변경처리", description = "username: 변경할 이름")
    @PutMapping("/setname")
    public String userNicknameChange(@RequestBody UserNicknameChange userNicknameChange, HttpServletRequest request , HttpServletResponse response) throws UnsupportedEncodingException {
        return userService.userNicknameChange(request,response, userNicknameChange);
    }
}
