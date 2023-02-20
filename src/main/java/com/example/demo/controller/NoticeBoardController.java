package com.example.demo.controller;

import com.example.demo.dto.BoardRequestDto;
import com.example.demo.dto.BoardResponseDto;
import com.example.demo.service.NoticeBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController("")
@RequiredArgsConstructor
public class NoticeBoardController {
    final private NoticeBoardService noticeBoardService;
    @GetMapping("/board/notice")
    public List<BoardResponseDto> getAllNotice(){

       return noticeBoardService.getAllService();
    }
    @PostMapping("/board/notice") //Create
    public String addNotice(@RequestBody BoardRequestDto boardRequestDto){

        return noticeBoardService.addService(boardRequestDto);
    }
    @GetMapping("/board/notice/{id}") //Read
    public BoardResponseDto getOneNotice(@PathVariable Long id){

        return noticeBoardService.getOneService(id);
    }

    @PutMapping ("/board/notice/{id}") //Update
    public String updateNotice(@PathVariable Long id,@RequestBody BoardRequestDto boardRequestDto) {

        return noticeBoardService.updateService(id,boardRequestDto);
    }

    @DeleteMapping("/board/notice/{id}") //Delete
    public String deleteNotice(@PathVariable Long id){

        return noticeBoardService.deleteService(id);
    }
}
