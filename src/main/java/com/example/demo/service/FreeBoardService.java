package com.example.demo.service;

import com.example.demo.dto.BoardRequestDto;
import com.example.demo.dto.BoardResponseDto;
import com.example.demo.entity.BoardEntity;
import com.example.demo.enumCustom.BoardType;
import com.example.demo.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FreeBoardService {
    final private BoardRepository boardRepository;

    @Transactional
    public List<BoardResponseDto> getAllService(){
        List<BoardEntity> boardEntityList = boardRepository.findByBoardType(BoardType.Free);
        return boardEntityList.stream().map(BoardResponseDto::new).collect(Collectors.toList());
    }
    @Transactional
    public String addService(BoardRequestDto boardRequestDto){
        try{
            BoardEntity boardEntity = BoardEntity.builder()
                    .boardType(BoardType.Free)
                    .title(boardRequestDto.getTitle())
                    .commentNum(0)
                    .name(boardRequestDto.getName())
                    .createTime(LocalDateTime.now())
                    .views(0)
                    .recommend(0)
                    .text(boardRequestDto.getText())
                    .build();
            boardRepository.save(boardEntity);
        }catch (NullPointerException e) {
            throw new NullPointerException("값이 NULL입니다.");
        }

        return "저장 완료";
    }
}
