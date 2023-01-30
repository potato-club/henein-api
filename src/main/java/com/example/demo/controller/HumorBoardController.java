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
    @GetMapping("/gethumorboard")
    public List<BoardResponseDto> getAllhumor(){

       return humorBoardService.getAllService();
    }
    @PostMapping("/posthumorboard") //Create
    public String addhumor(@RequestBody BoardRequestDto boardRequestDto){

        return humorBoardService.addService(boardRequestDto);
    }
    @GetMapping("/getreadhumor/{id}") //Read
    public BoardResponseDto getOnehumor(@PathVariable Long id){

        return humorBoardService.getOneService(id);
    }

    @PatchMapping ("/postupdatehumor/{id}") //Update
    public String updatehumor(@PathVariable Long id,@RequestBody BoardRequestDto boardRequestDto) {

        return humorBoardService.updateService(id,boardRequestDto);
    }

    @DeleteMapping("/postdeletehumor/{id}") //Delete
    public String deletehumor(@PathVariable Long id){

        return humorBoardService.deleteService(id);
    }
}
