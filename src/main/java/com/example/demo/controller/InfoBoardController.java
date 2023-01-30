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
    @GetMapping("/getinfoboard")
    public List<BoardResponseDto> getAllInfo(){

       return infoBoardService.getAllService();
    }
    @PostMapping("/postinfoboard") //Create
    public String addInfo(@RequestBody BoardRequestDto boardRequestDto){

        return infoBoardService.addService(boardRequestDto);
    }
    @GetMapping("/getreadinfo/{id}") //Read
    public BoardResponseDto getOneInfo(@PathVariable Long id){

        return infoBoardService.getOneService(id);
    }

    @PatchMapping ("/postupdateinfo/{id}") //Update
    public String updateInfo(@PathVariable Long id,@RequestBody BoardRequestDto boardRequestDto) {

        return infoBoardService.updateService(id,boardRequestDto);
    }

    @DeleteMapping("/postdeleteInfo/{id}")
    public String deleteInfo(@PathVariable Long id){

        return infoBoardService.deleteService(id);
    }
}
