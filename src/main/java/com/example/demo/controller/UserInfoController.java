package com.example.demo.controller;

import com.example.demo.dto.userchar.NodeConnection;
import com.example.demo.dto.userchar.UserCharDto;
import com.example.demo.dto.userchar.UserMapleApi;
import com.example.demo.dto.user.UserInfoResponseDto;

import com.example.demo.dto.user.UserNicknameChange;
import com.example.demo.error.ErrorCode;
import com.example.demo.error.exception.NotFoundException;
import com.example.demo.service.UserService;
import io.swagger.annotations.Api;

import io.swagger.v3.oas.annotations.Operation;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;


@RestController
@RequestMapping("/userinfo")
@RequiredArgsConstructor
@Api(tags = {"유저정보 제공 Controller"})
@Slf4j
public class UserInfoController {
    private final UserService userService;
    @Operation(summary = "유저 정보에 대한 요청 API [보안]")
    @GetMapping
    public UserInfoResponseDto userInfo(HttpServletRequest request, HttpServletResponse response){
        log.info("유저컨트롤러진입----------------------------------------------");
        return userService.userInfo(request, response);
    }
    //@Tag(name = "신규유저 이름변경처리", description = "username: 변경할 이름")
    @Operation(summary = "유저 이름 변경 API")
    @PutMapping("/set-name")
    public String userNicknameChange(@RequestBody UserNicknameChange userNickname, HttpServletRequest request , HttpServletResponse response) throws UnsupportedEncodingException {
        log.info("유저 이름 컨트롤러진입----------------------------------------------");
        String userName = userNickname.getUserName();
        if (userName == null || userName.length() < 2 || userName.length() >= 15) {
            throw new NotFoundException(ErrorCode.INVALID_USER,"유저이름이 너무 짧거나 깁니다");
        }
        return userService.userNicknameChange(request,response, userNickname);
    }
    @Operation(summary = "유저의 캐릭터 이름 요청")
    @PostMapping("/character-info")
    public Flux<String> getCharacterName (@RequestBody UserMapleApi userMapleApi){
        return userService.getCharacterName(userMapleApi);
    }
    //////////node/////
    @PostMapping("/test")
    public String test(@RequestBody NodeConnection nodeConnection){
        log.info(nodeConnection.getId()+"\n"+
                        nodeConnection.getUserCharDto().getNickname()+"\n"+
                nodeConnection.getUserCharDto().getExperience()+"\n"+
                nodeConnection.getUserCharDto().getAvatar()+"\n"+
                nodeConnection.getUserCharDto().getWorld()
                );
        return "보내준 데이터의 nickname은: "+nodeConnection.getUserCharDto().getNickname();
    }

}
