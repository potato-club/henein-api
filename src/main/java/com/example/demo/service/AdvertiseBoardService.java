package com.example.demo.service;

import com.example.demo.dto.BoardRequestDto;
import com.example.demo.dto.BoardResponseDto;
import com.example.demo.entity.AdvertiseBoardEntity;
import com.example.demo.repository.AdvertiseBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdvertiseBoardService {
    final private AdvertiseBoardRepository advertiseBoardRepository;

    @Transactional
    public List<BoardResponseDto> getAllService(){
        List<AdvertiseBoardEntity> advertiseBoardEntities = advertiseBoardRepository.findAll();
        return advertiseBoardEntities.stream().map(BoardResponseDto::new).collect(Collectors.toList());
    }
    @Transactional
    public String addService(BoardRequestDto boardRequestDto){
        try{
            AdvertiseBoardEntity advertiseBoardEntity = AdvertiseBoardEntity.builder()
                    .title(boardRequestDto.getTitle())
                    .commentNum(0)
                    .name(boardRequestDto.getName())
                    .createTime(LocalDateTime.now())
                    .views(0)
                    .recommend(0)
                    .text(boardRequestDto.getText())
                    .build();
            advertiseBoardRepository.save(advertiseBoardEntity);
        }catch (NullPointerException e) {
            throw new NullPointerException("값이 NULL입니다.");
        }

        return "저장 완료";
    }
    @Transactional
    public BoardResponseDto getOneService(Long id){
        AdvertiseBoardEntity advertiseBoardEntity = advertiseBoardRepository.findById(id).orElseThrow(()->{throw new RuntimeException("해당 정보가 없습니다");});
        BoardResponseDto boardResponseDto = new BoardResponseDto(advertiseBoardEntity);
        return boardResponseDto;
    }
    @Transactional
    public String updateService(Long id,BoardRequestDto boardRequestDto){
        AdvertiseBoardEntity advertiseBoardEntity = advertiseBoardRepository.findById(id).orElseThrow(()->{throw new RuntimeException("해당 정보가 없습니다");});
        try{
            advertiseBoardEntity.Update(boardRequestDto);
            advertiseBoardRepository.save(advertiseBoardEntity);
        } catch (NullPointerException e) {
            throw new NullPointerException("값이 NULL입니다.");
        }

        return "수정완료";
    }
    @Transactional
    public String deleteService(Long id){
        AdvertiseBoardEntity advertiseBoardEntity = advertiseBoardRepository.findById(id).orElseThrow(()->{throw new RuntimeException("해당 정보가 없습니다");});
        advertiseBoardRepository.delete(advertiseBoardEntity);
        return "삭제완료";
    }
}
