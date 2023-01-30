package com.example.demo.controller;

import com.example.demo.dto.BoardRequestDto;
import com.example.demo.dto.BoardResponseDto;
import com.example.demo.entity.FreeBoardEntity;
import com.example.demo.repository.FreeBoardRepository;
import com.example.demo.service.FreeBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
public class FreeBoardController {
    final private FreeBoardService freeBoardService;
    @GetMapping("/getfreeboard")
    public List<BoardResponseDto> getAllFree(){

       return freeBoardService.getAllService();
    }
    @PostMapping("/postfreeboard") //Create
    public String addFree(@RequestBody BoardRequestDto boardRequestDto){

        return freeBoardService.addService(boardRequestDto);
    }
    @GetMapping("/getreadfree/{id}") //Read
    public BoardResponseDto getOneFree(@PathVariable Long id){

        return freeBoardService.getOneService(id);
    }

    @PatchMapping ("/postupdatefree/{id}") //Update
    public String updateFree(@PathVariable Long id,@RequestBody BoardRequestDto boardRequestDto) {

        return freeBoardService.updateService(id,boardRequestDto);
    }

    @DeleteMapping("/postdeletefree/{id}")
    public String deleteFree(@PathVariable Long id){

        return freeBoardService.deleteService(id);
    }
}
