package com.example.demo.controller;

import com.example.demo.dto.BoardRequestDto;
import com.example.demo.dto.BoardResponseDto;
import com.example.demo.error.exception.NotFoundException;
import com.example.demo.service.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.error.ErrorCode.RUNTIME_EXCEPTION;


@RestController("")
@RequestMapping(value = "/board")
@RequiredArgsConstructor
public class BoardCategoryController {
    final private CommonBoardService commonBoardService;
    final private BoardTypeOfService boardTypeOfService;

    @GetMapping("/entireboard")
    public List<BoardResponseDto> getEntireBoard(){
        return boardTypeOfService.getEntireBoard();
    }

    @GetMapping("/{boardtype}")
    public List<BoardResponseDto> getBoard(@PathVariable char boardtype){
        int n = boardtype;
        switch (n){
            case 65: return boardTypeOfService.getAllServiceA();
            case 66: return boardTypeOfService.getAllServiceB();
            case 70: return boardTypeOfService.getAllServiceF();
            case 72: return boardTypeOfService.getAllServiceH();
            case 73: return boardTypeOfService.getAllServiceI();
            case 78: return boardTypeOfService.getAllServiceN();
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
    @GetMapping("/search/{id}") //Read
    public BoardResponseDto getOneBoard(@PathVariable("id") Long id){
        return commonBoardService.getOneService(id);
    }

    @PutMapping ("/search/{id}") //Update
    public String updateBoard(@PathVariable("id") Long id,@RequestBody BoardRequestDto boardRequestDto) {
        return commonBoardService.updateService(id, boardRequestDto);
    }

    @DeleteMapping("/search/{id}") //Delete
    public String deleteBoard(@PathVariable("id") Long id){
        return commonBoardService.deleteService(id);
    }
}
