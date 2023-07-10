package com.example.demo.controller;

import com.example.demo.dto.board.*;
import com.example.demo.entity.S3File;
import com.example.demo.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



@RestController("")
@RequestMapping(value = "/board")
@RequiredArgsConstructor
@Slf4j
@Api(tags = {"게시글 Controller"})

public class BoardController {

    private final CommonBoardService commonBoardService;
    private final BoardTypeOfService boardTypeOfService;
    private final S3Service s3Service;



    @ApiImplicitParams({
            @ApiImplicitParam(name="board", value= "원하는 게시판 타입[ex A,B,F,I,H,N,E[entireboard]]", required = true),
            @ApiImplicitParam(name = "page", value = "원하는 페이지 값", required = true)
    })
    @GetMapping()
    public Page<BoardListResponseDto> getTypeOfBoard(@RequestParam("board")char boardtype, @RequestParam("page")int page){
        return boardTypeOfService.getTypeOfBoard(page, boardtype);
    }
    //Read
    @GetMapping("/{id}")
    public BoardResponseDto getOneBoard(@PathVariable Long id, @RequestHeader(value = "Authorization",required = false)String authentication){
        return commonBoardService.getOneService(id, authentication);
    }
    //==================================================================================
    @Operation(summary = "Json 으로 보내주세요 [보안]")
    @PostMapping() //Create
    public String addTypeOfBoard(@RequestBody BoardRequestDto boardRequestDto,
                                 HttpServletRequest request, HttpServletResponse response) {
        log.info("작성 컨트롤러 진입");
        return boardTypeOfService.addTypeOfBoard(boardRequestDto, request, response);
    }
    @Operation(summary = "게시글 호출시에 같이 호출하여 게시판 조회수 업")
    @PostMapping("/updateview")
    public String updateView(@RequestBody BoardIdRequestDTO boardIdRequestDTO){

        return commonBoardService.updateView(boardIdRequestDTO.getId());
    }
    @Operation(summary = "[보안]")
    @PostMapping("/recommend")
    public String recommendThisBoard(@RequestBody BoardIdRequestDTO boardIdRequestDTO, HttpServletRequest request, HttpServletResponse response){
        return commonBoardService.recommendThisBoard(boardIdRequestDTO.getId(),request, response);
    }
    @Operation(summary = "사진 name return api")
    @PostMapping("/image")
    public String saveImageAndGetName(@RequestPart MultipartFile image) throws IOException {
        return s3Service.uploadImageBeforeSavedBoardEntity(image);
    }
//==================================================================================
    @Operation(summary = "[보안]")
    @PutMapping("/{id}")
    public String updateBoard(@PathVariable Long id, @RequestBody TestDto testDto, HttpServletRequest request, HttpServletResponse response){
        return commonBoardService.updateService(id, testDto, request, response);
    }
    @Operation(summary = "[보안]")
    @DeleteMapping("/{id}")
    public String deleteBoard(@PathVariable("id")Long id, HttpServletRequest request, HttpServletResponse response){
        return commonBoardService.deleteService(id, request, response);
    }

}
