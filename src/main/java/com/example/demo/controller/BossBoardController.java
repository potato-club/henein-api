package com.example.demo.controller;

import com.example.demo.dto.BoardRequestDto;
import com.example.demo.dto.BoardResponseDto;
import com.example.demo.service.BossBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class BossBoardController {
    final private BossBoardService bossBoardService;
    @GetMapping("/board/boss")
    public List<BoardResponseDto> getAllBoss(){

       return bossBoardService.getAllService();
    }
    @PostMapping("/board/boss") //Create
    public String addBoss(@RequestBody BoardRequestDto boardRequestDto){

        return bossBoardService.addService(boardRequestDto);
    }
    @GetMapping("/board/boss/{id}") //Read
    public BoardResponseDto getOneBoss(@PathVariable Long id){

        return bossBoardService.getOneService(id);
    }

    @PutMapping ("/board/boss/{id}") //Update
    public String updateBoss(@PathVariable Long id,@RequestBody BoardRequestDto boardRequestDto) {

        return bossBoardService.updateService(id,boardRequestDto);
    }

    @DeleteMapping("/board/boss/{id}") //Delete
    public String deleteBoss(@PathVariable Long id){

        return bossBoardService.deleteService(id);
    }
}
