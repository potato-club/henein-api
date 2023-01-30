package com.example.demo.service;

import com.example.demo.dto.BoardRequestDto;
import com.example.demo.dto.BoardResponseDto;
import com.example.demo.entity.HumorBoardEntity;
import com.example.demo.repository.HumorBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HumorBoardService {
    final private HumorBoardRepository humorBoardRepository;

    @Transactional
    public List<BoardResponseDto> getAllService(){
        List<HumorBoardEntity> humorBoardEntities = humorBoardRepository.findAll();
        return humorBoardEntities.stream().map(BoardResponseDto::new).collect(Collectors.toList());
    }
    @Transactional
    public String addService(BoardRequestDto boardRequestDto){
        try{
            HumorBoardEntity humorBoardEntity = HumorBoardEntity.builder()
                    .title(boardRequestDto.getTitle())
                    .commentNum(0)
                    .name(boardRequestDto.getName())
                    .createTime(LocalDateTime.now())
                    .views(0)
                    .recommend(0)
                    .text(boardRequestDto.getText())
                    .build();
            humorBoardRepository.save(humorBoardEntity);
        }catch (NullPointerException e) {
            throw new NullPointerException("값이 NULL입니다.");
        }

        return "저장 완료";
    }
    @Transactional
    public BoardResponseDto getOneService(Long id){
        HumorBoardEntity humorBoardEntity = humorBoardRepository.findById(id).orElseThrow(()->{throw new RuntimeException("해당 정보가 없습니다");});
        BoardResponseDto boardResponseDto = new BoardResponseDto(humorBoardEntity);
        return boardResponseDto;
    }
    @Transactional
    public String updateService(Long id,BoardRequestDto boardRequestDto){
        HumorBoardEntity humorBoardEntity = humorBoardRepository.findById(id).orElseThrow(()->{throw new RuntimeException("해당 정보가 없습니다");});
        try{
            humorBoardEntity.Update(boardRequestDto);
            humorBoardRepository.save(humorBoardEntity);
        } catch (NullPointerException e) {
            throw new NullPointerException("값이 NULL입니다.");
        }

        return "수정완료";
    }
    @Transactional
    public String deleteService(Long id){
        HumorBoardEntity humorBoardEntity = humorBoardRepository.findById(id).orElseThrow(()->{throw new RuntimeException("해당 정보가 없습니다");});
        humorBoardRepository.delete(humorBoardEntity);
        return "삭제완료";
    }
}
