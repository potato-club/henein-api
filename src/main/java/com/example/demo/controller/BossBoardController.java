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
    @GetMapping("/getbossboard")
    public List<BoardResponseDto> getAllBoss(){

       return bossBoardService.getAllService();
    }
    @PostMapping("/postbossboard") //Create
    public String addBoss(@RequestBody BoardRequestDto boardRequestDto){

        return bossBoardService.addService(boardRequestDto);
    }
    @GetMapping("/getreadboss/{id}") //Read
    public BoardResponseDto getOneBoss(@PathVariable Long id){

        return bossBoardService.getOneService(id);
    }

    @PatchMapping ("/postupdateboss/{id}") //Update
    public String updateBoss(@PathVariable Long id,@RequestBody BoardRequestDto boardRequestDto) {

        return bossBoardService.updateService(id,boardRequestDto);
    }

    @DeleteMapping("/postdeleteboss/{id}") //Delete
    public String deleteBoss(@PathVariable Long id){

        return bossBoardService.deleteService(id);
    }
}
