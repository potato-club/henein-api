package com.example.demo.service;

import com.example.demo.dto.BoardRequestDto;
import com.example.demo.dto.BoardResponseDto;
import com.example.demo.dto.RecommandUpdateDTO;
import com.example.demo.entity.BoardEntity;
import com.example.demo.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
@Service
@RequiredArgsConstructor
public class CommonBoardService {
    final private BoardRepository boardRepository;
    @Transactional
    public BoardResponseDto getOneService(Long id){
        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow(()->{throw new RuntimeException("해당 게시글 정보가 없습니다");});
        BoardResponseDto boardResponseDto = new BoardResponseDto(boardEntity);
        return boardResponseDto;
    }
    @Transactional
    public String updateService(Long id, BoardRequestDto boardRequestDto){
        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow(()->{throw new RuntimeException("해당 게시글 정보가 없습니다");});
        try{
            boardEntity.Update(boardRequestDto);
            boardRepository.save(boardEntity);
        } catch (NullPointerException e) {
            throw new NullPointerException("값이 NULL입니다.");
        }

        return "수정완료";
    }
    @Transactional
    public String deleteService(Long id){
        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow(()->{throw new RuntimeException("해당 게시글 정보가 없습니다");});
        boardRepository.delete(boardEntity);
        return "삭제완료";
    }
    /*@Transactional
    public String recommendThisBoard(Long id){
        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow(()->{throw new RuntimeException("해당 게시글 정보가 없습니다");});
        RecommandUpdateDTO recommandUpdateDTO = new RecommandUpdateDTO();
        recommandUpdateDTO.setRecommend(boardEntity.getRecommend()+1);
        boardEntity.Update(recommandUpdateDTO);
        boardRepository.save(boardEntity);

        return "추천성공";
    }
    @Transactional
    public String unRecommendThisBoard(Long id){
        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow(()->{throw new RuntimeException("해당 게시글 정보가 없습니다");});
        RecommandUpdateDTO recommandUpdateDTO = new RecommandUpdateDTO();
        recommandUpdateDTO.setRecommend(boardEntity.getRecommend()-1);
        boardEntity.Update(recommandUpdateDTO);
        boardRepository.save(boardEntity);
    }*/
}
