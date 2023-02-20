package com.example.demo.controller;

import com.example.demo.dto.BoardRequestDto;
import com.example.demo.dto.BoardResponseDto;
import com.example.demo.service.HumorBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class HumorBoardController {
    final private HumorBoardService humorBoardService;
    @GetMapping("/board/humor")
    public List<BoardResponseDto> getAllhumor(){

       return humorBoardService.getAllService();
    }
    @PostMapping("/board/humor") //Create
    public String addhumor(@RequestBody BoardRequestDto boardRequestDto){

        return humorBoardService.addService(boardRequestDto);
    }
    @GetMapping("/board/humor/{id}") //Read
    public BoardResponseDto getOnehumor(@PathVariable Long id){

        return humorBoardService.getOneService(id);
    }

    @PutMapping ("/board/humor/{id}") //Update
    public String updatehumor(@PathVariable Long id,@RequestBody BoardRequestDto boardRequestDto) {

        return humorBoardService.updateService(id,boardRequestDto);
    }

    @DeleteMapping("/board/humor/{id}") //Delete
    public String deletehumor(@PathVariable Long id){

        return humorBoardService.deleteService(id);
    }
}
