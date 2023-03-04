package com.example.demo.service;

import com.example.demo.dto.CommentRequsetDto;
import com.example.demo.dto.CommentResponseDto;
import com.example.demo.dto.CommentNumUpdateDto;
import com.example.demo.entity.BoardEntity;
import com.example.demo.entity.CommentEntity;
import com.example.demo.error.exception.InternerServerException;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.CommentRepository;
import com.sun.jdi.InternalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.error.ErrorCode;

@Service
@RequiredArgsConstructor
public class CommentService {
    final private CommentRepository commentRepository;
    final private BoardRepository boardRepository;
    @Transactional
    public List<CommentResponseDto> getCommentOfId(Long id){
        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow(()->{throw new RuntimeException("해당 게시글이 없습니다");});
        List<CommentEntity> commentEntityList = commentRepository.findByBoardEntity(id);

        return commentEntityList.stream().map(CommentResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public String postCommentOfId(Long id,CommentRequsetDto commentRequsetDto){
        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow(()->{throw new RuntimeException("해당 댓글이 없습니다");});

        CommentEntity commentEntity = CommentEntity.builder()
                .boardEntity(boardEntity)
                .userName(commentRequsetDto.getUserName())
                .text(commentRequsetDto.getText())
                .build();
        //보드 게시판의 댓글수 업데이트
        CommentNumUpdateDto commentNumUpdateDto = new CommentNumUpdateDto();
        commentNumUpdateDto.setCommentNum(boardEntity.getCommentNum()+1);
        boardEntity.Update(commentNumUpdateDto);

        commentRepository.save(commentEntity);
        boardRepository.save(boardEntity);

        return "작성완료";
    }
    @Transactional
    public String updateCommentOfId(Long coid,CommentRequsetDto commentRequsetDto){
        CommentEntity commentEntity = commentRepository.findById(coid).orElseThrow(()->{throw new RuntimeException("해당 댓글이 없습니다");});
        commentEntity.Update(commentRequsetDto);
        commentRepository.save(commentEntity);
        return "수정 완료";
    }
    @Transactional
    public String deleteCommentOfId(Long id, Long coid){
        CommentEntity commentEntity = commentRepository.findById(coid).orElseThrow(()->{throw new RuntimeException("해당 댓글이 없습니다");});
        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow(()->{throw new RuntimeException("해당 댓글이 없습니다");});
        //보드 게시판의 댓글수 업데이트
        if(boardEntity.getCommentNum() > 0) {
            CommentNumUpdateDto commentNumUpdateDto = new CommentNumUpdateDto();
            commentNumUpdateDto.setCommentNum(boardEntity.getCommentNum() - 1);
            boardEntity.Update(commentNumUpdateDto);
        }
        commentRepository.delete(commentEntity);
        return "삭제완료";
    }
}
