package com.example.demo.controller;

import com.example.demo.dto.BoardRequestDto;
import com.example.demo.dto.BoardResponseDto;
import com.example.demo.error.exception.NotFoundException;
import com.example.demo.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.error.ErrorCode.RUNTIME_EXCEPTION;


@RestController("")
@RequestMapping(value = "/board/{boardtype}")
@RequiredArgsConstructor
public class BoardCategoryController {
    final private CommonBoardService commonBoardService;
    final private AdvertiseBoardService advertiseBoardService;
    final private BossBoardService bossBoardService;
    final private FreeBoardService freeBoardService;
    final private HumorBoardService humorBoardService;
    final private InfoBoardService infoBoardService;
    final private NoticeBoardService noticeBoardService;

    @GetMapping()
    public List<BoardResponseDto> getBoard(@PathVariable char boardtype){
        int n = boardtype;
        switch (n){
            case 65: return advertiseBoardService.getAllService();
            case 66: return bossBoardService.getAllService();
            case 70: return freeBoardService.getAllService();
            case 72: return humorBoardService.getAllService();
            case 73: return infoBoardService.getAllService();
            case 78: return noticeBoardService.getAllService();
            default: throw new NotFoundException(RUNTIME_EXCEPTION,"E00000");
        }
    }
    @PostMapping() //Create
    public String addBoard(@PathVariable char boardtype, @RequestBody BoardRequestDto boardRequestDto){
        int n = boardtype;
        switch (n){
            case 65: return advertiseBoardService.addService(boardRequestDto);
            case 66: return bossBoardService.addService(boardRequestDto);
            case 70: return freeBoardService.addService(boardRequestDto);
            case 72: return humorBoardService.addService(boardRequestDto);
            case 73: return infoBoardService.addService(boardRequestDto);
            case 78: return noticeBoardService.addService(boardRequestDto);
            default: throw new NotFoundException(RUNTIME_EXCEPTION,"E00000");
        }
    }
    @GetMapping("/{id}") //Read
    public BoardResponseDto getOneBoard(@PathVariable("id") Long id){
        return commonBoardService.getOneService(id);
    }

    @PutMapping ("/{id}") //Update
    public String updateBoard(@PathVariable("id") Long id,@RequestBody BoardRequestDto boardRequestDto) {
        return commonBoardService.updateService(id, boardRequestDto);
    }

    @DeleteMapping("/{id}") //Delete
    public String deleteBoard(@PathVariable("id") Long id){
        return commonBoardService.deleteService(id);
    }
}
