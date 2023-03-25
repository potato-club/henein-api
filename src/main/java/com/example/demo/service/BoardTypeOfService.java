package com.example.demo.service;

import com.example.demo.dto.board.BoardRequestDto;
import com.example.demo.dto.board.BoardResponseDto;
import com.example.demo.entity.BoardEntity;
import com.example.demo.enumCustom.BoardType;
import com.example.demo.error.exception.NotFoundException;
import com.example.demo.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static com.example.demo.error.ErrorCode.RUNTIME_EXCEPTION;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardTypeOfService {
    final private BoardRepository boardRepository;

/*    @Transactional //전체게시판
    public Page<BoardResponseDto> getEntireBoard(int page){
        //id를 이용한 내림차순 정렬
        PageRequest pageRequest = PageRequest.of(page-1, 20);
        Page<BoardEntity> boardEntityList = boardRepository.findAllNotNotice(pageRequest);
        return new PageImpl<>(boardEntityList.stream().map(BoardResponseDto::new).collect(Collectors.toList()));
    }*/

    @Transactional //
    public Page<BoardResponseDto> getTypeOfBoard(int page, int boardType){
        BoardType board;
        PageRequest pageRequest = PageRequest.of(page-1, 20);

        switch (boardType){
            case 65: board = BoardType.Advertise; break;
            case 66: board = BoardType.Boss; break;
            case 69: {Page<BoardEntity> boardEntityList = boardRepository.findAllNotNotice(pageRequest);
                return new PageImpl<>(boardEntityList.stream().map(BoardResponseDto::new).collect(Collectors.toList()));}
            case 70: board = BoardType.Free; break;
            case 72: board = BoardType.Humor; break;
            case 73: board = BoardType.Info; break;
            case 78: board = BoardType.Notice; break;
            default: throw new NotFoundException(RUNTIME_EXCEPTION,"E00000");
        }

        Page<BoardEntity> boardEntityList = boardRepository.findByBoardType(board,pageRequest);
        return new PageImpl<>(boardEntityList.stream().map(BoardResponseDto::new).collect(Collectors.toList()));
    }

    //===================================================================================================
    @Transactional
    public String addTypeOfBoard(int boardType, BoardRequestDto boardRequestDto){
        BoardType board;
        switch (boardType){
            case 65: board = BoardType.Advertise; break;
            case 66: board = BoardType.Boss; break;
            case 70: board = BoardType.Free; break;
            case 72: board = BoardType.Info; break;
            case 73: board = BoardType.Humor; break;
            case 78: board = BoardType.Notice; break;
            default: throw new NotFoundException(RUNTIME_EXCEPTION,"E00000");
        }
        try{
            BoardEntity boardEntity = BoardEntity.builder()
                    .boardType(board)
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