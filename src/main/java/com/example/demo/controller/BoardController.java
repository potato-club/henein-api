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
@CrossOrigin(origins = "http://localhost:3000")
public class BoardController {

    private final CommonBoardService commonBoardService;
    private final BoardTypeOfService boardTypeOfService;

    @PostMapping("/updateview")
    public String updateView(@RequestBody BoardRequestDto boardRequestDto){

        return commonBoardService.updateView(boardRequestDto.getId());
    }
    @ApiImplicitParams({
            @ApiImplicitParam(name="board", value= "원하는 게시판 타입[ex A,B,F,I,H,N,E[entireboard]]", required = true),
            @ApiImplicitParam(name = "page", value = "원하는 페이지 값", required = true)
    })
    //@modelattrivute로 dto를 만들어서 한번에 처리할 수도 있다.
    @GetMapping()
    public Page<BoardResponseDto> getTypeOfBoard(@RequestParam("board")char boardtype, @RequestParam("page")int page){
        return boardTypeOfService.getTypeOfBoard(page, boardtype);
    }

    @PostMapping() //Create
    public String addTypeOfBoard(@RequestBody BoardRequestDto boardRequestDto, List<MultipartFile> image){
        return boardTypeOfService.addTypeOfBoard(image,boardRequestDto);
    }
    //Read
    @GetMapping("/{id}")
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

    //추천 로직
    @PostMapping("/recommend")
    public String recommendThisBoard(@RequestBody BoardRequestDto boardRequestDto, HttpServletRequest request){
        return commonBoardService.recommendThisBoard(boardRequestDto.getId(),request);
    }
}
