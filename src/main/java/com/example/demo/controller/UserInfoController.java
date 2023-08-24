package com.example.demo.controller;

import com.example.demo.dto.board.BoardListResponseDto;
import com.example.demo.dto.userchar.NodeConnection;
import com.example.demo.dto.user.UserInfoResponseDto;

import com.example.demo.dto.user.UserNicknameChange;
import com.example.demo.dto.userchar.UserCharacter;
import com.example.demo.dto.userchar.UserMapleApi;
import com.example.demo.error.ErrorCode;
import com.example.demo.error.exception.NotFoundException;
import com.example.demo.service.UserService;
import io.swagger.annotations.Api;

import io.swagger.v3.oas.annotations.Operation;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;


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
    //=====================메이플 캐릭터 관련=========================//
    @Operation(summary = "현재 인증된 모든 캐릭터 가져오기")
    @GetMapping("/character/all")
    public List<UserCharacter> getAllUserCharacterInfo(HttpServletRequest request,HttpServletResponse response){
        return userService.getAllUserCharacterInfo(request,response);
    }

    @Operation(summary = "대표 캐릭터 설정")
    @PostMapping("/character/pick")
    public void pickCharacter(@RequestParam Long id, HttpServletRequest request, HttpServletResponse response){
        userService.pickCharacter(id,request,response);
    }
    @Operation(summary = "단일 캐릭터 정보갱신 요청")
    @GetMapping("/character/renew")
    public String requestUpdateChar(@RequestParam String name){
        return userService.requestUpdateToNode(name);
    }
    @Operation(summary = "유저가 가지고있는 캐릭터 큐브 내역으로 불러오기" )
    @PostMapping("/character/auth") String requestNexon(@RequestBody UserMapleApi userMapleApi,HttpServletResponse response,HttpServletRequest request){
        return userService.requestToNexon(request,response,userMapleApi);
    }
    @Operation(summary = "노드에서 spring으로 요청할 api")
    @PostMapping("/character/info")
    public String test(@RequestBody NodeConnection nodeConnection){

        return userService.responseToRedisAndUpdate(nodeConnection);
    }

    //================내 활동 관련 =====================//
    @Operation(summary = "내가 쓴 게시글 보기")
    @GetMapping("/myboards")
    public List<BoardListResponseDto> getMyBoardList (HttpServletRequest request, HttpServletResponse response) {
        return userService.getMyBoardList(request, response);
    }
    @Operation(summary = "댓글 작성한 게시글 보기")
    @GetMapping("/mycomment-boards")
    public List<BoardListResponseDto> getMyBoardsWithCommentList (HttpServletRequest request, HttpServletResponse response) {
        return userService.getMyBoardsWithCommentList(request, response);
    }

}
