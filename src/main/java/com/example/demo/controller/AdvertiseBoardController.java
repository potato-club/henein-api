package com.example.demo.controller;

import com.example.demo.dto.BoardRequestDto;
import com.example.demo.dto.BoardResponseDto;
import com.example.demo.service.AdvertiseBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController("")
@RequiredArgsConstructor
public class AdvertiseBoardController {
    final private AdvertiseBoardService advertiseBoardService;
    @GetMapping("/board/advertise")
    public List<BoardResponseDto> getAlladvertise(){

       return advertiseBoardService.getAllService();
    }
    @PostMapping("/board/advertise") //Create
    public String addadvertise(@RequestBody BoardRequestDto boardRequestDto){

        return advertiseBoardService.addService(boardRequestDto);
    }
    @GetMapping("/board/advertise/{id}") //Read
    public BoardResponseDto getOneadvertise(@PathVariable Long id){

        return advertiseBoardService.getOneService(id);
    }

    @PutMapping ("/board/advertise/{id}") //Update
    public String updateadvertise(@PathVariable Long id,@RequestBody BoardRequestDto boardRequestDto) {

        return advertiseBoardService.updateService(id,boardRequestDto);
    }

    @DeleteMapping("/board/advertise/{id}") //Delete
    public String deleteadvertise(@PathVariable Long id){

        return advertiseBoardService.deleteService(id);
    }
}
