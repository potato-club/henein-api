package com.example.demo.controller;

import com.example.demo.dto.board.BoardRequestDto;
import com.example.demo.dto.board.BoardResponseDto;
import com.example.demo.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


@RestController("")
@RequestMapping(value = "/board")
@RequiredArgsConstructor
@Slf4j
@Api(tags = {"게시글 Controller"})
public class BoardController {

    private final CommonBoardService commonBoardService;
    private final BoardTypeOfService boardTypeOfService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "원하는 페이지 값", required = true)
    })
    @GetMapping("/entire") // 전체게시판 ( 공지게시판 호출없음)
    public Page<BoardResponseDto> getEntireBoard(@RequestParam("page")int page){

        return boardTypeOfService.getEntireBoard(page);
    }
    @PostMapping("/{id}/updateview")
    public String updateView(@PathVariable Long id){
        return commonBoardService.updateView(id);
    }
    @ApiImplicitParams({
            @ApiImplicitParam(name="board", value= "원하는 게시판 타입[ex(A,B,F,I,H,N)]", required = true),
            @ApiImplicitParam(name = "page", value = "원하는 페이지 값", required = true)
    })
    //@modelattrivute로 dto를 만들어서 한번에 처리할 수도 있다.
    @GetMapping()
    public Page<BoardResponseDto> getTypeOfBoard(@RequestParam("board")char boardtype, @RequestParam("page")int page){
        return boardTypeOfService.getTypeOfBoard(page, boardtype);
    }
    @ApiImplicitParams({
            @ApiImplicitParam(name="board", value= "원하는 게시판 타입[ex(A,B,F,I,H,N)]", required = true),
    })
    @PostMapping() //Create
    public String addTypeOfBoard(@RequestParam("board")char boardtype, @RequestBody BoardRequestDto boardRequestDto){
        return boardTypeOfService.addTypeOfBoard(boardtype,boardRequestDto);
    }
    @GetMapping("/{id}") //Read
    public BoardResponseDto getOneBoard(@PathVariable("id") Long id){
        return commonBoardService.getOneService(id);
    }

    @PutMapping("/{id}")
    public String updateBoard(@PathVariable("id")Long id,@RequestBody BoardRequestDto boardRequestDto){
        return commonBoardService.updateService(id, boardRequestDto);
    }
    @DeleteMapping("/{id}")
    public String deleteBoard(@PathVariable("id")Long id){
        return commonBoardService.deleteService(id);
    }

    /*@PatchMapping("/{boardtype}/{id}/recommend")
    public String recommendThisBoard(@PathVariable("id")Long id){return commonBoardService.recommendThisBoard(id);}
    @PatchMapping("/{boardtype}/{id}/unRecommend")
    public String unRecommendThisBoard(@PathVariable("id")Long id){return commonBoardService.unRecommendThisBoard(id);}*/
}
