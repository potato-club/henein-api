package com.example.demo.service;

import com.example.demo.dto.comment.CommentRequsetDto;
import com.example.demo.dto.comment.CommentResponseDto;
import com.example.demo.dto.comment.CommentNumUpdateDto;
import com.example.demo.entity.*;
import com.example.demo.error.ErrorCode;

import com.example.demo.error.exception.NotFoundException;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.ReplyRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    final private CommentRepository commentRepository;
    final private ReplyRepository replyRepository;
    final private BoardRepository boardRepository;
    final private JPAQueryFactory jpaQueryFactory;

    @Transactional
    public List<CommentResponseDto> getCommentOfBoard(Long boardId) {
        if (!boardRepository.existsById(boardId)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION,ErrorCode.NOT_FOUND_EXCEPTION.getMessage());
        }
        QCommentEntity qCommentEntity = QCommentEntity.commentEntity;
        QReplyEntity qReplyEntity = QReplyEntity.replyEntity;

        List<CommentEntity> commentEntityList = jpaQueryFactory.select(qCommentEntity)
                .from(qCommentEntity)
                .where(qCommentEntity.boardEntity.id.eq(boardId))//부모댓글부터 가져옴
                .orderBy(qCommentEntity.id.asc())
                .fetch();

        List<CommentResponseDto> resultDtoList = new ArrayList<>();
        for (CommentEntity parentComment : commentEntityList){
            List<ReplyEntity> childComment = getChildComment(parentComment);

            CommentResponseDto parentDto = new CommentResponseDto(parentComment);
            parentDto.setReplies(childComment.stream().map(CommentResponseDto::new).collect(Collectors.toList()));
            resultDtoList.add(parentDto);
        }
        return resultDtoList;
    }


    private List<ReplyEntity> getChildComment(CommentEntity commentEntity){
        QReplyEntity qReplyEntity = QReplyEntity.replyEntity;
        QCommentEntity qCommentEntity = QCommentEntity.commentEntity;

        List<ReplyEntity> childList = jpaQueryFactory.select(qReplyEntity)
                .from(qReplyEntity)
                .leftJoin(qReplyEntity.parent,qCommentEntity)
                .where(qReplyEntity.parent.id.eq(commentEntity.getId()))
                .orderBy(qCommentEntity.id.asc())
                .fetch();
        return childList;
    }

    @Transactional
    public String addCommentOfParent(CommentRequsetDto commentRequsetDto, HttpServletRequest request ){
        BoardEntity boardEntity = boardRepository.findById(commentRequsetDto.getBoardId()).orElseThrow(()->{throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION,ErrorCode.NOT_FOUND_EXCEPTION.getMessage());});
        CommentEntity commentEntity = CommentEntity.builder()
                .comment(commentRequsetDto.getComment())
                .userName("댓글작성유저")
                .boardEntity(boardEntity)
                .updated(false)
                .build();
        commentRepository.save(commentEntity);

        //보드 게시판의 댓글수 업데이트
        CommentNumUpdateDto commentNumUpdateDto = new CommentNumUpdateDto();
        commentNumUpdateDto.setCommentNum(boardEntity.getCommentNum()+1);
        boardEntity.Update(commentNumUpdateDto);

        boardRepository.save(boardEntity);
        return "댓글 작성 완료";
    }
    @Transactional
    public String addCommentOfChild(CommentRequsetDto commentRequsetDto, HttpServletRequest request){
        BoardEntity boardEntity = boardRepository.findById(commentRequsetDto.getBoardId()).orElseThrow(()->{throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION,ErrorCode.NOT_FOUND_EXCEPTION.getMessage());});
        CommentEntity parentComment = commentRepository.findById(commentRequsetDto.getCommentId()).orElseThrow(()->{throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION,ErrorCode.NOT_FOUND_EXCEPTION.getMessage());});

        ReplyEntity replyEntity = ReplyEntity.builder()
                .tag(commentRequsetDto.getTag())
                .comment(commentRequsetDto.getComment())
                .userName("대댓글작성유저")
                .parent(parentComment)
                .updated(false)
                .build();
        replyRepository.save(replyEntity);


        //보드 게시판의 댓글수 업데이트
        CommentNumUpdateDto commentNumUpdateDto = new CommentNumUpdateDto();
        commentNumUpdateDto.setCommentNum(boardEntity.getCommentNum()+1);
        boardEntity.Update(commentNumUpdateDto);

        boardRepository.save(boardEntity);
        return "대댓글 작성 완료";
    }

    @Transactional
    public String updateCommentOfParent(CommentRequsetDto commentRequsetDto, HttpServletRequest request){
        if (!boardRepository.existsById(commentRequsetDto.getBoardId())) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION,ErrorCode.NOT_FOUND_EXCEPTION.getMessage());
        }
        CommentEntity commentEntity = commentRepository.findById(commentRequsetDto.getCommentId()).orElseThrow(()->{throw new RuntimeException("해당 댓글이 없습니다");});
        commentEntity.update(commentRequsetDto);
        commentRepository.save(commentEntity);
        return "수정 완료";
    }
    @Transactional
    public String updateCommentOfChild(CommentRequsetDto commentRequsetDto, HttpServletRequest request){
        ReplyEntity replyEntity = replyRepository.findById(commentRequsetDto.getCommentId()).orElseThrow(()->{throw new RuntimeException("해당 댓글이 없습니다");});
        replyEntity.update(commentRequsetDto);
        replyRepository.save(replyEntity);
        return "수정 완료";
    }

    @Transactional
    public String deleteCommentOfParent(CommentRequsetDto commentRequsetDto, HttpServletRequest request){
        CommentEntity commentEntity = commentRepository.findById(commentRequsetDto.getCommentId()).orElseThrow(()->{throw new RuntimeException("해당 댓글이 없습니다");});
        BoardEntity boardEntity = boardRepository.findById(commentRequsetDto.getBoardId()).orElseThrow(()->{throw new RuntimeException("해당 댓글이 없습니다");});
        //보드 게시판의 댓글수 업데이트
        if(boardEntity.getCommentNum() > 0) {
            CommentNumUpdateDto commentNumUpdateDto = new CommentNumUpdateDto();
            commentNumUpdateDto.setCommentNum(boardEntity.getCommentNum() - 1);
            boardEntity.Update(commentNumUpdateDto);
        }
        commentRepository.delete(commentEntity);
        return "삭제완료";
    }
    @Transactional
    public String deleteCommentOfChild(CommentRequsetDto commentRequsetDto, HttpServletRequest request){
        CommentEntity commentEntity = commentRepository.findById(commentRequsetDto.getCommentId()).orElseThrow(()->{throw new RuntimeException("해당 댓글이 없습니다");});
        BoardEntity boardEntity = boardRepository.findById(commentRequsetDto.getBoardId()).orElseThrow(()->{throw new RuntimeException("해당 댓글이 없습니다");});
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
