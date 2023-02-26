package com.example.demo.controller;

import com.example.demo.dto.BoardRequestDto;
import com.example.demo.dto.BoardResponseDto;
import com.example.demo.service.FreeBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class FreeBoardController {
    final private FreeBoardService freeBoardService;
    @GetMapping("/board/free")
    public List<BoardResponseDto> getAllFree(){

       return freeBoardService.getAllService();
    }
    @PostMapping("/board/free") //Create
    public String addFree(@RequestBody BoardRequestDto boardRequestDto){

        return freeBoardService.addService(boardRequestDto);
    }
    @GetMapping("/board/free/{id}") //Read
    public BoardResponseDto getOneFree(@PathVariable Long id){

        return freeBoardService.getOneService(id);
    }

    @PutMapping ("/board/free/{id}") //Update
    public String updateFree(@PathVariable Long id,@RequestBody BoardRequestDto boardRequestDto) {

        return freeBoardService.updateService(id,boardRequestDto);
    }

    @DeleteMapping("/board/free/{id}")
    public String deleteFree(@PathVariable Long id){

        return freeBoardService.deleteService(id);
    }
}
