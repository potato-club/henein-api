package com.example.demo.controller;

import com.example.demo.dto.board.BoardIdRequestDTO;
import com.example.demo.dto.board.BoardRequestDto;
import com.example.demo.dto.board.BoardResponseDto;
import com.example.demo.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController("")
@RequestMapping(value = "/board")
@RequiredArgsConstructor
@Slf4j
@Api(tags = {"게시글 Controller"})

public class BoardController {

    private final CommonBoardService commonBoardService;
    private final BoardTypeOfService boardTypeOfService;

    @PostMapping("/updateview")
    public String updateView(@RequestBody BoardIdRequestDTO boardIdRequestDTO){

        return commonBoardService.updateView(boardIdRequestDTO.getId());
    }
//    @ApiImplicitParams({
//            @ApiImplicitParam(name="board", value= "원하는 게시판 타입[ex A,B,F,I,H,N,E[entireboard]]", required = true),
//            @ApiImplicitParam(name = "page", value = "원하는 페이지 값", required = true)
//    })
    //@modelattrivute로 dto를 만들어서 한번에 처리할 수도 있다.
    @GetMapping()
    public Page<BoardResponseDto> getTypeOfBoard(@RequestParam("board")char boardtype, @RequestParam("page")int page){
        return boardTypeOfService.getTypeOfBoard(page, boardtype);
    }
    @Tag(name = "게시글 작성(이미지저장 기능 추가 )",description = "Json이 아닌 from-data형식으로 보내줘야행")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "image", value = "image", required = true, dataType = "file", paramType = "form"),
            @ApiImplicitParam(name = "title", value = "title", required = true, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "name", value = "name", required = true, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "text", value = "text", required = true, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "boardType", value = "boardType", required = true, dataType = "string", paramType = "form")
    })
    @PostMapping() //Create
    public String addTypeOfBoard(List<MultipartFile> image, BoardRequestDto boardRequestDto) {
        return boardTypeOfService.addTypeOfBoard(image,boardRequestDto);
    }
    //Read
    @GetMapping("/{id}")
    public BoardResponseDto getOneBoard(@PathVariable Long id){
        return commonBoardService.getOneService(id);
    }

    @PutMapping("/{id}")
    public String updateBoard(@PathVariable Long id,@RequestBody BoardRequestDto boardRequestDto){
        return commonBoardService.updateService(id, boardRequestDto);
    }
    @DeleteMapping("/{id}")
    public String deleteBoard(@PathVariable("id")Long id){
        return commonBoardService.deleteService(id);
    }

    //추천 로직
    @PostMapping("/recommend")
    public String recommendThisBoard(@RequestBody BoardIdRequestDTO boardIdRequestDTO, HttpServletRequest request){
        return commonBoardService.recommendThisBoard(boardIdRequestDTO.getId(),request);
    }
}
