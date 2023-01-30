package com.example.demo.service;

import com.example.demo.dto.BoardRequestDto;
import com.example.demo.dto.BoardResponseDto;
import com.example.demo.entity.InfoBoardEntity;
import com.example.demo.repository.InfoBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InfoBoardService {
    final private InfoBoardRepository infoBoardRepository;

    @Transactional
    public List<BoardResponseDto> getAllService(){
        List<InfoBoardEntity> infoBoardEntities = infoBoardRepository.findAll();
        return infoBoardEntities.stream().map(BoardResponseDto::new).collect(Collectors.toList());
    }
    @Transactional
    public String addService(BoardRequestDto boardRequestDto){
        try{
            InfoBoardEntity infoBoardEntity = InfoBoardEntity.builder()
                    .title(boardRequestDto.getTitle())
                    .commentNum(0)
                    .name(boardRequestDto.getName())
                    .createTime(LocalDateTime.now())
                    .views(0)
                    .recommend(0)
                    .text(boardRequestDto.getText())
                    .build();
            infoBoardRepository.save(infoBoardEntity);
        }catch (NullPointerException e) {
            throw new NullPointerException("값이 NULL입니다.");
        }

        return "저장 완료";
    }
    @Transactional
    public BoardResponseDto getOneService(Long id){
        InfoBoardEntity infoBoardEntity = infoBoardRepository.findById(id).orElseThrow(()->{throw new RuntimeException("해당 정보가 없습니다");});
        BoardResponseDto boardResponseDto = new BoardResponseDto(infoBoardEntity);
        return boardResponseDto;
    }
    @Transactional
    public String updateService(Long id,BoardRequestDto boardRequestDto){
        InfoBoardEntity infoBoardEntity = infoBoardRepository.findById(id).orElseThrow(()->{throw new RuntimeException("해당 정보가 없습니다");});
        try{
            infoBoardEntity.Update(boardRequestDto);
            infoBoardRepository.save(infoBoardEntity);
        } catch (NullPointerException e) {
            throw new NullPointerException("값이 NULL입니다.");
        }

        return "수정완료";
    }
    @Transactional
    public String deleteService(Long id){
        InfoBoardEntity infoBoardEntity = infoBoardRepository.findById(id).orElseThrow(()->{throw new RuntimeException("해당 정보가 없습니다");});
        infoBoardRepository.delete(infoBoardEntity);
        return "삭제완료";
    }
}
