package com.example.demo.controller;

import com.example.demo.dto.BoardRequestDto;
import com.example.demo.dto.BoardResponseDto;
import com.example.demo.service.AdvertiseBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class AdvertiseBoardController {
    final private AdvertiseBoardService advertiseBoardService;
    @GetMapping("/getadvertiseboard")
    public List<BoardResponseDto> getAlladvertise(){

       return advertiseBoardService.getAllService();
    }
    @PostMapping("/postadvertiseboard") //Create
    public String addadvertise(@RequestBody BoardRequestDto boardRequestDto){

        return advertiseBoardService.addService(boardRequestDto);
    }
    @GetMapping("/getreadadvertise/{id}") //Read
    public BoardResponseDto getOneadvertise(@PathVariable Long id){

        return advertiseBoardService.getOneService(id);
    }

    @PatchMapping ("/postupdateadvertise/{id}") //Update
    public String updateadvertise(@PathVariable Long id,@RequestBody BoardRequestDto boardRequestDto) {

        return advertiseBoardService.updateService(id,boardRequestDto);
    }

    @DeleteMapping("/postdeleteadvertise/{id}") //Delete
    public String deleteadvertise(@PathVariable Long id){

        return advertiseBoardService.deleteService(id);
    }
}
