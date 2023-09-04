package com.example.demo.controller;

import com.example.demo.dto.board.BoardListResponseDto;
import com.example.demo.dto.user.UserDetailInfoResponseDto;
import com.example.demo.dto.user.UserInfoResponseDto;
import com.example.demo.dto.userchar.NodeConnection;

import com.example.demo.dto.user.UserInfoUpdate;
import com.example.demo.dto.userchar.UserCharacter;
import com.example.demo.dto.userchar.UserMapleApi;
import com.example.demo.error.ErrorCode;
import com.example.demo.error.exception.ForbiddenException;
import com.example.demo.error.exception.NotFoundException;
import com.example.demo.service.UserService;
import io.swagger.annotations.Api;

import io.swagger.v3.oas.annotations.Operation;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;


@RestController
@RequestMapping("/userinfo")
@RequiredArgsConstructor
@Api(tags = {"유저정보 제공 Controller"})
@Slf4j
public class UserInfoController {
    private final UserService userService;

    @Operation(summary = "유저 정보에 대한 요청 API")
    @GetMapping
    public UserInfoResponseDto userInfo(HttpServletRequest request){

        return userService.userInfo(request);
    }
    @Operation(summary = "유저 정보에 대한 요청 API")
    @GetMapping("/profile")
    public UserDetailInfoResponseDto userDetailInfo(HttpServletRequest request){

        return userService.userDetailInfo(request);
    }
//    @Operation(summary = "유저 사진 변경 API")
//    @PutMapping("/set-picture")
//    public void updateUserPicture(MultipartFile image, HttpServletRequest request) throws IOException {
//        userService.updateUserPicture(image, request);
//    }
    @Operation(summary = "유저 이름,사진 변경 API - [form-data]")
    @PutMapping()
    public String userUpdate(@RequestPart MultipartFile image, @RequestPart String userName, HttpServletRequest request) throws IOException {

        if (userName == null || userName.length() < 2 || userName.length() >= 15) {
            throw new ForbiddenException("이름이 너무 짧거나 깁니다.",ErrorCode.BAD_REQUEST);
        }
        return userService.userUpdate(image,userName, request);
    }
    //=====================메이플 캐릭터 관련=========================//
    @Operation(summary = "현재 인증된 모든 캐릭터 가져오기")
    @GetMapping("/character/all")
    public List<UserCharacter> getAllUserCharacterInfo(HttpServletRequest request){
        return userService.getAllUserCharacterInfo(request);
    }

    @Operation(summary = "대표 캐릭터 설정")
    @PostMapping("/character/pick")
    public void pickCharacter(@RequestParam Long id, HttpServletRequest request){
        userService.pickCharacter(id,request);
    }
    @Operation(summary = "단일 캐릭터 정보갱신 요청")
    @GetMapping("/character/renew")
    public String requestUpdateChar(@RequestParam String name){
        return userService.requestUpdateToNode(name);
    }
    @Operation(summary = "유저가 가지고있는 캐릭터 큐브 내역으로 불러오기" )
    @PostMapping("/character/auth") String requestNexon(@RequestBody UserMapleApi userMapleApi,HttpServletRequest request){
        return userService.requestToNexon(request,userMapleApi);
    }
    @Operation(summary = "노드에서 spring으로 요청할 api")
    @PostMapping("/character/info")
    public String test(@RequestBody NodeConnection nodeConnection){

        return userService.responseToRedisAndUpdate(nodeConnection);
    }

    //================내 활동 관련 =====================//
    @Operation(summary = "내가 쓴 게시글 보기")
    @GetMapping("/myboards")
    public List<BoardListResponseDto> getMyBoardList (HttpServletRequest request) {
        return userService.getMyBoardList(request);
    }
    @Operation(summary = "댓글 작성한 게시글 보기")
    @GetMapping("/mycomment-boards")
    public List<BoardListResponseDto> getMyBoardsWithCommentList (HttpServletRequest request) {
        return userService.getMyBoardsWithCommentList(request);
    }

}
