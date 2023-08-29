package com.example.demo.service;

import com.example.demo.dto.comment.*;
import com.example.demo.entity.*;
import com.example.demo.error.ErrorCode;

import com.example.demo.error.exception.NotFoundException;
import com.example.demo.error.exception.UnAuthorizedException;
import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.ReplyRepository;
import com.example.demo.repository.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;



    @Transactional
    public List<CommentResponseDto> getCommentOfBoard(Long boardId, String authentication) {
        if (!boardRepository.existsById(boardId)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION.getMessage(), ErrorCode.NOT_FOUND_EXCEPTION);
        }
        UserEntity userEntity;
        if (authentication != null) {

            authentication = authentication.substring(7);
            String userEmail = jwtTokenProvider.getUserEmailFromAccessToken(authentication); // 정보 가져옴
            userEntity = userRepository.findByUserEmail(userEmail).
                    orElseThrow(() -> new UnAuthorizedException(ErrorCode.INVALID_ACCESS.getMessage(),ErrorCode.INVALID_ACCESS));
        } else {
            userEntity = null;
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
            CommentResponseDto parentDto;
            if ( userEntity != null) {
               parentDto = new CommentResponseDto(parentComment, userEntity.getUid());
               parentDto.setReplies(childComment.stream()
                        .map(replyEntity -> new ReplyResponseDto(replyEntity,userEntity.getUid()))
                        .collect(Collectors.toList()));
            } else {
                parentDto = new CommentResponseDto(parentComment);
                parentDto.setReplies(childComment.stream()
                        .map(replyEntity -> new ReplyResponseDto(replyEntity))
                        .collect(Collectors.toList()));
            }

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
    public String addCommentOfParent(Long id,CommentRequsetDto commentRequsetDto, HttpServletRequest request){
        UserEntity userEntity = userService.fetchUserEntityByHttpRequest(request);
        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow(()->{throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION.getMessage(), ErrorCode.NOT_FOUND_EXCEPTION);});
        CommentEntity commentEntity = CommentEntity.builder()
                .comment(commentRequsetDto.getComment())
                .userName(userEntity.getUserName())
                .userEmail(userEntity.getUserEmail())
                .uid(userEntity.getUid())
                .boardEntity(boardEntity)
                .updated(false)
                .deleted(false)
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
    public String addCommentOfChild(Long id,Long coId, ReplyRequestDto replyRequestDto, HttpServletRequest request){
        UserEntity userEntity = userService.fetchUserEntityByHttpRequest(request);
        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow(()->{throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION.getMessage(), ErrorCode.NOT_FOUND_EXCEPTION);});
        CommentEntity parentComment = commentRepository.findById(coId).orElseThrow(()->{throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION.getMessage(), ErrorCode.NOT_FOUND_EXCEPTION);});

        ReplyEntity replyEntity = ReplyEntity.builder()
                .tag(replyRequestDto.getTag())
                .comment(replyRequestDto.getComment())
                .uid(userEntity.getUid())
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
    public String updateCommentOfParent(Long id,Long coId,CommentRequsetDto commentRequsetDto, HttpServletRequest request){
        UserEntity userEntity = userService.fetchUserEntityByHttpRequest(request);
        if (!boardRepository.existsById(id)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION.getMessage(), ErrorCode.NOT_FOUND_EXCEPTION);
        }
        CommentEntity commentEntity = commentRepository.findById(coId).orElseThrow(()->{throw new RuntimeException("해당 댓글이 없습니다");});
        if (!commentEntity.getUserEmail().equals(userEntity.getUserEmail())) {
            throw new RuntimeException("권한이 없는 사용자 입니다.");
        }
        commentEntity.update(commentRequsetDto,userEntity.getUserName());
        return "수정 완료";
    }
    @Transactional
    public String updateCommentOfChild(Long id,Long reId,ReplyRequestDto replyRequestDto, HttpServletRequest request){
        UserEntity userEntity = userService.fetchUserEntityByHttpRequest(request);
        if (!boardRepository.existsById(id)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION.getMessage(), ErrorCode.NOT_FOUND_EXCEPTION);
        }
        ReplyEntity replyEntity = replyRepository.findById(reId).orElseThrow(()->{throw new RuntimeException("해당 댓글이 없습니다");});
        if (!replyEntity.getUserEmail().equals(userEntity.getUserEmail())){
            throw new RuntimeException("권한이 없는 사용자 입니다.");
        }
        replyEntity.update(replyRequestDto,userEntity.getUserName());
        return "수정 완료";
    }

    @Transactional
    public String deleteCommentOfParent(Long id,Long coId, HttpServletRequest request ){
        UserEntity userEntity = userService.fetchUserEntityByHttpRequest(request);
        CommentEntity commentEntity = commentRepository.findById(coId).orElseThrow(()->{throw new RuntimeException("해당 댓글이 없습니다");});
        if (!(commentEntity.getUserEmail().equals(userEntity.getUserEmail()))){
            throw new RuntimeException("권한이 없는 사용자 입니다.");
        }
        //자식이 있으면 deleted == true
        if (commentEntity.getReplies() != null){
            commentEntity.delete();
            decreaseBoardCommentNum(id);
            return "임시 삭제완료";
        }else{
            decreaseBoardCommentNum(id);
            commentRepository.delete(commentEntity);
            return "삭제완료";
        }

    }
    @Transactional
    public String deleteCommentOfChild(Long id,Long reId,HttpServletRequest request){
        UserEntity userEntity = userService.fetchUserEntityByHttpRequest(request);
        ReplyEntity replyEntity = replyRepository.findById(reId).orElseThrow(()->{throw new RuntimeException("해당 댓글이 없습니다");});
        if (!(replyEntity.getUserEmail().equals(userEntity.getUserEmail()))){
            throw new RuntimeException("권한이 없는 사용자 입니다");
        }

        decreaseBoardCommentNum(id);
        if (replyEntity.getParent().getDeleted()){
            CommentEntity commentEntity = replyEntity.getParent();
            replyRepository.delete(replyEntity);
            if (commentEntity.getReplies().isEmpty()){
                commentRepository.delete(commentEntity);
            }
            return "삭제완료";
        }
        replyRepository.delete(replyEntity);


        return "삭제완료";
    }
    public void decreaseBoardCommentNum(Long id){
        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow(()->{throw new RuntimeException("해당 게시글이 없습니다");});
        //보드 게시판의 댓글수 업데이트
        if(boardEntity.getCommentNum() > 0) {
            CommentNumUpdateDto commentNumUpdateDto = new CommentNumUpdateDto();
            commentNumUpdateDto.setCommentNum(boardEntity.getCommentNum() - 1);
            boardEntity.Update(commentNumUpdateDto);
        }
    }
}
