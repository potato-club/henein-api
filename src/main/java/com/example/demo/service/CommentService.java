package com.example.demo.service;

import com.example.demo.dto.comment.*;
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
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final UserService userService;


    @Transactional
    public List<CommentResponseDto> getCommentOfBoard(Long boardId) {
        if (!boardRepository.existsById(boardId)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION,ErrorCode.NOT_FOUND_EXCEPTION.getMessage());
        }
        QCommentEntity qCommentEntity = QCommentEntity.commentEntity;

        List<CommentEntity> commentEntityList = jpaQueryFactory.select(qCommentEntity)
                .from(qCommentEntity)
                .where(qCommentEntity.boardEntity.id.eq(boardId))//부모댓글부터 가져옴
                .orderBy(qCommentEntity.id.asc())
                .fetch();

        List<CommentResponseDto> resultDtoList = new ArrayList<>();
        for (CommentEntity parentComment : commentEntityList){
            List<ReplyEntity> childComment = getChildComment(parentComment);

            CommentResponseDto parentDto = new CommentResponseDto(parentComment);
            parentDto.setReplies(childComment.stream().map(ReplyResponseDto::new).collect(Collectors.toList()));
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
                .orderBy(qReplyEntity.id.asc())
                .fetch();
        return childList;
    }
///////////////////////////////

    @Transactional
    public String addCommentOfParent(Long id,CommentRequsetDto commentRequsetDto, HttpServletRequest request, HttpServletResponse response){
        UserEntity userEntity = userService.fetchUserEntityByHttpRequest(request,response);
        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow(()->{throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION,ErrorCode.NOT_FOUND_EXCEPTION.getMessage());});
        CommentEntity commentEntity = CommentEntity.builder()
                .comment(commentRequsetDto.getComment())
                .userName(userEntity.getUserName())
                .userEmail(userEntity.getUserEmail())
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
    public String addCommentOfChild(Long id,Long coId, ReplyRequestDto replyRequestDto, HttpServletRequest request, HttpServletResponse response){
        UserEntity userEntity = userService.fetchUserEntityByHttpRequest(request,response);
        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow(()->{throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION,ErrorCode.NOT_FOUND_EXCEPTION.getMessage());});
        CommentEntity parentComment = commentRepository.findById(coId).orElseThrow(()->{throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION,ErrorCode.NOT_FOUND_EXCEPTION.getMessage());});

        ReplyEntity replyEntity = ReplyEntity.builder()
                .tag(replyRequestDto.getTag())
                .comment(replyRequestDto.getComment())
                .userName(userEntity.getUserName())
                .userEmail(userEntity.getUserEmail())
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
    public String updateCommentOfParent(Long id,Long coId,CommentRequsetDto commentRequsetDto, HttpServletRequest request, HttpServletResponse response){
        UserEntity userEntity = userService.fetchUserEntityByHttpRequest(request,response);
        if (!boardRepository.existsById(id)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION,ErrorCode.NOT_FOUND_EXCEPTION.getMessage());
        }
        CommentEntity commentEntity = commentRepository.findById(coId).orElseThrow(()->{throw new RuntimeException("해당 댓글이 없습니다");});
        if (!commentEntity.getUserEmail().equals(userEntity.getUserEmail())) {
            throw new RuntimeException("권한이 없는 사용자 입니다.");
        }
        commentEntity.update(commentRequsetDto,userEntity.getUserName());
        return "수정 완료";
    }
    @Transactional
    public String updateCommentOfChild(Long id,Long reId,ReplyRequestDto replyRequestDto, HttpServletRequest request, HttpServletResponse response){
        UserEntity userEntity = userService.fetchUserEntityByHttpRequest(request,response);
        if (!boardRepository.existsById(id)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION,ErrorCode.NOT_FOUND_EXCEPTION.getMessage());
        }
        ReplyEntity replyEntity = replyRepository.findById(reId).orElseThrow(()->{throw new RuntimeException("해당 댓글이 없습니다");});
        if (!replyEntity.getUserEmail().equals(userEntity.getUserEmail())){
            throw new RuntimeException("권한이 없는 사용자 입니다.");
        }
        replyEntity.update(replyRequestDto,userEntity.getUserName());
        return "수정 완료";
    }

    @Transactional
    public String deleteCommentOfParent(Long id,Long coId, HttpServletRequest request, HttpServletResponse response){
        UserEntity userEntity = userService.fetchUserEntityByHttpRequest(request,response);
        CommentEntity commentEntity = commentRepository.findById(coId).orElseThrow(()->{throw new RuntimeException("해당 댓글이 없습니다");});
        if (!(commentEntity.getUserEmail().equals(userEntity.getUserEmail()))){
            throw new RuntimeException("권한이 없는 사용자 입니다.");
        }

        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow(()->{throw new RuntimeException("해당 게시글이 없습니다");});
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
    public String deleteCommentOfChild(Long id,Long reId,HttpServletRequest request, HttpServletResponse response){
        UserEntity userEntity = userService.fetchUserEntityByHttpRequest(request,response);
        ReplyEntity replyEntity = replyRepository.findById(reId).orElseThrow(()->{throw new RuntimeException("해당 댓글이 없습니다");});
        if (!(replyEntity.getUserEmail().equals(userEntity.getUserEmail()))){
            throw new RuntimeException("권한이 없는 사용자 입니다");
        }

        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow(()->{throw new RuntimeException("해당 게시글이 없습니다");});
        //보드 게시판의 댓글수 업데이트
        if(boardEntity.getCommentNum() > 0) {
            CommentNumUpdateDto commentNumUpdateDto = new CommentNumUpdateDto();
            commentNumUpdateDto.setCommentNum(boardEntity.getCommentNum() - 1);
            boardEntity.Update(commentNumUpdateDto);
        }
        replyRepository.delete(replyEntity);
        return "삭제완료";
    }

}
