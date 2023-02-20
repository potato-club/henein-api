package com.example.demo.controller;

import com.example.demo.dto.BoardRequestDto;
import com.example.demo.dto.BoardResponseDto;
import com.example.demo.service.InfoBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class InfoBoardController {
    final private InfoBoardService infoBoardService;
    @GetMapping("/board/info")
    public List<BoardResponseDto> getAllInfo(){

       return infoBoardService.getAllService();
    }
    @PostMapping("/board/info") //Create
    public String addInfo(@RequestBody BoardRequestDto boardRequestDto){

        return infoBoardService.addService(boardRequestDto);
    }
    @GetMapping("/board/info/{id}") //Read
    public BoardResponseDto getOneInfo(@PathVariable Long id){

        return infoBoardService.getOneService(id);
    }

    @PutMapping ("/board/info/{id}") //Update
    public String updateInfo(@PathVariable Long id,@RequestBody BoardRequestDto boardRequestDto) {

        return infoBoardService.updateService(id,boardRequestDto);
    }

    @DeleteMapping("/board/info/{id}")
    public String deleteInfo(@PathVariable Long id){

        return infoBoardService.deleteService(id);
    }
}
