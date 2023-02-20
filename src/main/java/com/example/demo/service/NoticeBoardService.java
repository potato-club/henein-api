package com.example.demo.service;

import com.example.demo.dto.BoardRequestDto;
import com.example.demo.dto.BoardResponseDto;
import com.example.demo.entity.NoticeBoardEntity;
import com.example.demo.repository.NoticeBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeBoardService {
    final private NoticeBoardRepository noticeBoardRepository;

    @Transactional
    public List<BoardResponseDto> getAllService(){
        List<NoticeBoardEntity> noticeBoardEntities = noticeBoardRepository.findAll();
        return noticeBoardEntities.stream().map(BoardResponseDto::new).collect(Collectors.toList());
    }
    @Transactional
    public String addService(BoardRequestDto boardRequestDto){
        try{
            NoticeBoardEntity NoticeBoardEntity = noticeBoardRepository.builder()
                    .title(boardRequestDto.getTitle())
                    .commentNum(0)
                    .name(boardRequestDto.getName())
                    .createTime(LocalDateTime.now())
                    .views(0)
                    .recommend(0)
                    .text(boardRequestDto.getText())
                    .build();
            noticeBoardRepository.save(NoticeBoardEntity);
        }catch (NullPointerException e) {
            throw new NullPointerException("값이 NULL입니다.");
        }

        return "저장 완료";
    }
    @Transactional
    public BoardResponseDto getOneService(Long id){
        NoticeBoardEntity noticeBoardEntity = noticeBoardRepository.findById(id).orElseThrow(()->{throw new RuntimeException("해당 정보가 없습니다");});
        BoardResponseDto boardResponseDto = new BoardResponseDto(NoticeBoardEntity);
        return boardResponseDto;
    }
    @Transactional
    public String updateService(Long id,BoardRequestDto boardRequestDto){
        NoticeBoardEntity NoticeBoardEntity = noticeBoardRepository.findById(id).orElseThrow(()->{throw new RuntimeException("해당 정보가 없습니다");});
        try{
            NoticeBoardEntity.Update(boardRequestDto);
            noticeBoardRepository.save(NoticeBoardEntity);
        } catch (NullPointerException e) {
            throw new NullPointerException("값이 NULL입니다.");
        }

        return "수정완료";
    }
    @Transactional
    public String deleteService(Long id){
        NoticeBoardEntity NoticeBoardEntity = noticeBoardRepository.findById(id).orElseThrow(()->{throw new RuntimeException("해당 정보가 없습니다");});
        noticeBoardRepository.delete(NoticeBoardEntity);
        return "삭제완료";
    }
}
