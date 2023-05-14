package com.example.demo.controller;

import com.example.demo.dto.board.BoardIdRequestDTO;
import com.example.demo.dto.board.BoardRequestDto;
import com.example.demo.dto.board.BoardResponseDto;
import com.example.demo.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "게시글 호출시에 같이 호출하여 게시판 조회수 업")
    @PostMapping("/updateview")
    public String updateView(@RequestBody BoardIdRequestDTO boardIdRequestDTO){

        return commonBoardService.updateView(boardIdRequestDTO.getId());
    }
    @ApiImplicitParams({
            @ApiImplicitParam(name="board", value= "원하는 게시판 타입[ex A,B,F,I,H,N,E[entireboard]]", required = true),
            @ApiImplicitParam(name = "page", value = "원하는 페이지 값", required = true)
    })
    @GetMapping()
    public Page<BoardResponseDto> getTypeOfBoard(@RequestParam("board")char boardtype, @RequestParam("page")int page){
        return boardTypeOfService.getTypeOfBoard(page, boardtype);
    }
    @Operation(summary = "Json이 아닌 form-data형식으로 보내주세요")
    @PostMapping() //Create
    public String addTypeOfBoard(@RequestParam("image") List<MultipartFile> image,
                                 @RequestParam("title") String title,
                                 @RequestParam("text") String text,
                                 @RequestParam("boardType") String boardType) {
        BoardRequestDto boardRequestDto = new BoardRequestDto();
        boardRequestDto.setTitle(title);
        boardRequestDto.setText(text);
        boardRequestDto.setBoardType(boardType);

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
