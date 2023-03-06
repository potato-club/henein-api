package com.example.demo.controller;

import com.example.demo.dto.BoardRequestDto;
import com.example.demo.dto.BoardResponseDto;
import com.example.demo.error.exception.NotFoundException;
import com.example.demo.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.error.ErrorCode.RUNTIME_EXCEPTION;


@RestController("")
@RequestMapping(value = "/board")
@RequiredArgsConstructor
public class BoardController {
    final private CommonBoardService commonBoardService;
    final private BoardTypeOfService boardTypeOfService;

    @GetMapping("/entireboard") // 전체게시판 ( 공지게시판 호출없음)
    public Page<BoardResponseDto> getEntireBoard(@RequestParam("page")int page){
        return boardTypeOfService.getEntireBoard(page);
    }

    @GetMapping("/{boardtype}")
    public Page<BoardResponseDto> getBoard(@PathVariable char boardtype, @RequestParam("page")int page){
        int n = boardtype;
        switch (n){
            case 65: return boardTypeOfService.getAllServiceA(page);
            case 66: return boardTypeOfService.getAllServiceB(page);
            case 70: return boardTypeOfService.getAllServiceF(page);
            case 72: return boardTypeOfService.getAllServiceH(page);
            case 73: return boardTypeOfService.getAllServiceI(page);
            case 78: return boardTypeOfService.getAllServiceN(page);
            default: throw new NotFoundException(RUNTIME_EXCEPTION,"E00000");
        }
    }
    @PostMapping("/{boardtype}") //Create
    public String addBoard(@PathVariable char boardtype, @RequestBody BoardRequestDto boardRequestDto){
        int n = boardtype;
        switch (n){
            case 65: return boardTypeOfService.addServiceA(boardRequestDto);
            case 66: return boardTypeOfService.addServiceB(boardRequestDto);
            case 70: return boardTypeOfService.addServiceF(boardRequestDto);
            case 72: return boardTypeOfService.addServiceH(boardRequestDto);
            case 73: return boardTypeOfService.addServiceI(boardRequestDto);
            case 78: return boardTypeOfService.addServiceN(boardRequestDto);
            default: throw new NotFoundException(RUNTIME_EXCEPTION,"E00000");
        }
    }
    @GetMapping("/{boardtype}/{id}")
    public BoardResponseDto getOneBoardOfType(@PathVariable("id")Long id){
        return commonBoardService.getOneService(id);
    }
    @GetMapping("/search/{id}") //Read
    public BoardResponseDto getOneBoard(@PathVariable("id") Long id){
        return commonBoardService.getOneService(id);
    }

    @PutMapping("/{boardtype}/{id}")
    public String updateBoardOfType(@PathVariable("id")Long id,@RequestBody BoardRequestDto boardRequestDto){
        return commonBoardService.updateService(id, boardRequestDto);
    }
    @PutMapping ("/search/{id}") //Update
    public String updateBoard(@PathVariable("id") Long id,@RequestBody BoardRequestDto boardRequestDto) {
        return commonBoardService.updateService(id, boardRequestDto);
    }
    @DeleteMapping("/{boardtype}/{id}")
    public String deleteBoardOfType(@PathVariable("id")Long id){
        return commonBoardService.deleteService(id);
    }
    @DeleteMapping("/search/{id}") //Delete
    public String deleteBoard(@PathVariable("id") Long id){
        return commonBoardService.deleteService(id);
    }

    /*@PatchMapping("/{boardtype}/{id}/recommend")
    public String recommendThisBoard(@PathVariable("id")Long id){return commonBoardService.recommendThisBoard(id);}
    @PatchMapping("/{boardtype}/{id}/unRecommend")
    public String unRecommendThisBoard(@PathVariable("id")Long id){return commonBoardService.unRecommendThisBoard(id);}*/
}
