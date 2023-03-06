package com.example.demo.service;

import com.example.demo.dto.BoardRequestDto;
import com.example.demo.dto.BoardResponseDto;
import com.example.demo.entity.BoardEntity;
import com.example.demo.enumCustom.BoardType;
import com.example.demo.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardTypeOfService {
    final private BoardRepository boardRepository;

    @Transactional //전체게시판
    public Page<BoardResponseDto> getEntireBoard(int page){
        //id를 이용한 내림차순 정렬
        PageRequest pageRequest = PageRequest.of(page-1, 20);
        Page<BoardEntity> boardEntityList = boardRepository.findAllNotNotice(pageRequest);

        return new PageImpl<>(boardEntityList.stream().map(BoardResponseDto::new).collect(Collectors.toList()));
    }

    @Transactional
    public Page<BoardResponseDto> getAllServiceA(int page){
        PageRequest pageRequest = PageRequest.of(page-1, 20);
        Page<BoardEntity> boardEntityList = boardRepository.findByBoardType(BoardType.Advertise,pageRequest);
        return new PageImpl<>(boardEntityList.stream().map(BoardResponseDto::new).collect(Collectors.toList()));
    }
    @Transactional
    public Page<BoardResponseDto> getAllServiceB(int page){
        PageRequest pageRequest = PageRequest.of(page-1, 20);
        Page<BoardEntity> boardEntityList = boardRepository.findByBoardType(BoardType.Boss,pageRequest);
        return new PageImpl<>(boardEntityList.stream().map(BoardResponseDto::new).collect(Collectors.toList()));
    }
    @Transactional
    public Page<BoardResponseDto> getAllServiceF(int page){
        PageRequest pageRequest = PageRequest.of(page-1, 20);
        Page<BoardEntity> boardEntityList = boardRepository.findByBoardType(BoardType.Free,pageRequest);
        return new PageImpl<>(boardEntityList.stream().map(BoardResponseDto::new).collect(Collectors.toList()));
    }
    @Transactional
    public Page<BoardResponseDto> getAllServiceI(int page){
        PageRequest pageRequest = PageRequest.of(page-1, 20);
        Page<BoardEntity> boardEntityList = boardRepository.findByBoardType(BoardType.Info,pageRequest);
        return new PageImpl<>(boardEntityList.stream().map(BoardResponseDto::new).collect(Collectors.toList()));
    }
    @Transactional
    public Page<BoardResponseDto> getAllServiceH(int page){
        PageRequest pageRequest = PageRequest.of(page-1, 20);
        Page<BoardEntity> boardEntityList = boardRepository.findByBoardType(BoardType.Humor,pageRequest);
        return new PageImpl<>(boardEntityList.stream().map(BoardResponseDto::new).collect(Collectors.toList()));
    }
    @Transactional
    public Page<BoardResponseDto> getAllServiceN(int page){
        PageRequest pageRequest = PageRequest.of(page-1, 20);
        Page<BoardEntity> boardEntityList = boardRepository.findByBoardType(BoardType.Notice,pageRequest);
        return new PageImpl<>(boardEntityList.stream().map(BoardResponseDto::new).collect(Collectors.toList()));
    }
    //===================================================================================================
    @Transactional
    public String addServiceA(BoardRequestDto boardRequestDto){
        try{
            BoardEntity boardEntity = BoardEntity.builder()
                    .boardType(BoardType.Advertise)
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
    @Transactional
    public String addServiceB(BoardRequestDto boardRequestDto){
        try{
            BoardEntity boardEntity = BoardEntity.builder()
                    .boardType(BoardType.Boss)
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
    @Transactional
    public String addServiceF(BoardRequestDto boardRequestDto){
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
    @Transactional
    public String addServiceH(BoardRequestDto boardRequestDto){
        try{
            BoardEntity boardEntity = BoardEntity.builder()
                    .boardType(BoardType.Humor)
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
    @Transactional
    public String addServiceI(BoardRequestDto boardRequestDto){
        try{
            BoardEntity boardEntity = BoardEntity.builder()
                    .boardType(BoardType.Info)
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
    @Transactional
    public String addServiceN(BoardRequestDto boardRequestDto){
        try{
            BoardEntity boardEntity = BoardEntity.builder()
                    .boardType(BoardType.Notice)
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
/*boardEntityList.stream()
                .filter(o->o.getBoardType().equals(BoardType.Notice))
                .collect(Collectors.toList())
                .forEach(li->{boardEntityList.remove(li);});*/