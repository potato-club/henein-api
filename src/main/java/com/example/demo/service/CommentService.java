package com.example.demo.service;

import com.example.demo.dto.comment.*;
import com.example.demo.entity.*;
import com.example.demo.enumCustom.UserRole;
import com.example.demo.error.ErrorCode;

import com.example.demo.error.exception.ForbiddenException;
import com.example.demo.error.exception.NotFoundException;
import com.example.demo.error.exception.UnAuthorizedException;
import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.repository.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class CommentService {
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final BoardCommentNumberingRepository boardCommentNumberingRepository;



    @Transactional
    public NumberingWithCommentResponseDto getCommentOfBoard(Long boardId, String authentication) {
        if (!boardRepository.existsById(boardId)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION.getMessage(), ErrorCode.NOT_FOUND_EXCEPTION);
        }
        UserEntity userEntity;
        if (authentication != null) {
            authentication = authentication.substring(7);
            jwtTokenProvider.validateToken(authentication);

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
            CommentResponseDto parentDto = new CommentResponseDto(parentComment);

            if ( userEntity != null) {
                if ( parentComment.getDeleted() ){
                    parentDto.setUid("deleted");
                } else {
                    if ( userEntity.getUid().equals(parentComment.getUid()))
                        parentDto.setUid(userEntity.getUid());
                }
               parentDto.setReplies(childComment.stream()
                        .map(replyEntity -> new ReplyResponseDto(replyEntity,userEntity.getUid()))
                        .collect(Collectors.toList()));
            } else {
                if ( parentComment.getDeleted() ){
                    parentDto.setUid("deleted");
                }
                parentDto.setReplies(childComment.stream()
                        .map(replyEntity -> new ReplyResponseDto(replyEntity))
                        .collect(Collectors.toList()));
            }

            resultDtoList.add(parentDto);
        }

        List<BoardCommentNumberingEntity> numberingEntityList = boardCommentNumberingRepository.findAllByBoardId(boardId);
        Map<String, Integer> numberingResult = new HashMap<>();
        for (BoardCommentNumberingEntity b : numberingEntityList) {
            numberingResult.put(b.getUserEmail(),b.getUserNumbering());
        }

        return new NumberingWithCommentResponseDto(numberingResult,resultDtoList);
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
    public String addCommentOfParent(Long id, CommentRequestDto commentRequestDto, HttpServletRequest request){
        String userEmail = jwtTokenProvider.fetchUserEmailByHttpRequest(request);
        UserEntity userEntity = userRepository.findByUserEmail(userEmail).orElseThrow(()->{throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION.getMessage(), ErrorCode.NOT_FOUND_EXCEPTION);});

        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow(()->{throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION.getMessage(), ErrorCode.NOT_FOUND_EXCEPTION);});
        UserRole roleInBoard = setRoleInBoard(userEntity,boardEntity.getUserEntity());

        CommentEntity commentEntity = CommentEntity.builder()
                .comment(commentRequestDto.getComment())
                .userName(userEntity.getUserName())
                .userEmail(userEntity.getUserEmail())
                .roleInBoard(roleInBoard)
                .uid(userEntity.getUid())
                .boardEntity(boardEntity)
                .updated(false)
                .deleted(false)
                .build();

        commentRepository.save(commentEntity);

        this.checkBoardCommentNumbering(boardEntity.getId(),userEmail);
        //보드 게시판의 댓글수 업데이트
        boardEntity.Update(boardEntity.getCommentNum()+1);

        boardRepository.save(boardEntity);
        return "댓글 작성 완료";
    }


    @Transactional
    public String addCommentOfChild(Long id,Long coId, ReplyRequestDto replyRequestDto, HttpServletRequest request){
        String userEmail = jwtTokenProvider.fetchUserEmailByHttpRequest(request);
        UserEntity userEntity = userRepository.findByUserEmail(userEmail).orElseThrow(()->{throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION.getMessage(), ErrorCode.NOT_FOUND_EXCEPTION);});

        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow(()->{throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION.getMessage(), ErrorCode.NOT_FOUND_EXCEPTION);});
        CommentEntity parentComment = commentRepository.findById(coId).orElseThrow(()->{throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION.getMessage(), ErrorCode.NOT_FOUND_EXCEPTION);});
        UserRole roleInBoard = setRoleInBoard(userEntity,boardEntity.getUserEntity());

        ReplyEntity replyEntity = ReplyEntity.builder()
                .tag(replyRequestDto.getTag())
                .comment(replyRequestDto.getComment())
                .uid(userEntity.getUid())
                .userName(userEntity.getUserName())
                .roleInBoard(roleInBoard)
                .userEmail(userEntity.getUserEmail())
                .parent(parentComment)
                .updated(false)
                .build();

        replyRepository.save(replyEntity);
        this.checkBoardCommentNumbering(boardEntity.getId(),userEmail);

        //보드 게시판의 댓글수 업데이트

        boardEntity.Update(boardEntity.getCommentNum()+1);

        boardRepository.save(boardEntity);
        return "대댓글 작성 완료";
    }
    private void checkBoardCommentNumbering(long boardId, String userEmail) {
        List<BoardCommentNumberingEntity> numberingEntityList = boardCommentNumberingRepository.findAllByBoardId(boardId);
        if (!numberingEntityList.isEmpty()) {
            for ( BoardCommentNumberingEntity numberingEntity : numberingEntityList) {
                if ( numberingEntity.getUserEmail().equals(userEmail) ) {
                    return;
                }
            }

            BoardCommentNumberingEntity numberingEntity = new BoardCommentNumberingEntity(boardId,userEmail,numberingEntityList.get(numberingEntityList.size()-1).getUserNumbering()+1);
            boardCommentNumberingRepository.save(numberingEntity);
            return;
        }
        BoardCommentNumberingEntity numberingEntity = new BoardCommentNumberingEntity(boardId,userEmail,1);
        boardCommentNumberingRepository.save(numberingEntity);
    }
    private UserRole setRoleInBoard(UserEntity userEntity, UserEntity writerEntity) {
        if (userEntity.getUserRole().equals(UserRole.ADMIN)) {
            return UserRole.ADMIN;
        }
        else if (userEntity.equals(writerEntity)) {
            return UserRole.WRITER;
        }
        else {
            return UserRole.USER;
        }
    }

    @Transactional
    public String updateCommentOfParent(Long id, Long coId, CommentRequestDto commentRequestDto, HttpServletRequest request){
        String userEmail = jwtTokenProvider.fetchUserEmailByHttpRequest(request);

        if (!boardRepository.existsById(id)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION.getMessage(), ErrorCode.NOT_FOUND_EXCEPTION);
        }
        CommentEntity commentEntity = commentRepository.findById(coId).orElseThrow(()->{throw new NotFoundException("해당 댓글이 없습니다.",ErrorCode.NOT_FOUND_EXCEPTION);});
        if (!commentEntity.getUserEmail().equals(userEmail)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_EXCEPTION.getMessage(), ErrorCode.FORBIDDEN_EXCEPTION);
        }
        commentEntity.update(commentRequestDto);
        return "수정 완료";
    }
    @Transactional
    public String updateCommentOfChild(Long id,Long reId,ReplyRequestDto replyRequestDto, HttpServletRequest request){
        String userEmail = jwtTokenProvider.fetchUserEmailByHttpRequest(request);
        if (!boardRepository.existsById(id)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION.getMessage(), ErrorCode.NOT_FOUND_EXCEPTION);
        }
        ReplyEntity replyEntity = replyRepository.findById(reId).orElseThrow(()->{throw new NotFoundException("해당 댓글이 없습니다.",ErrorCode.NOT_FOUND_EXCEPTION);});
        if (!replyEntity.getUserEmail().equals(userEmail)){
            throw new ForbiddenException(ErrorCode.FORBIDDEN_EXCEPTION.getMessage(), ErrorCode.FORBIDDEN_EXCEPTION);
        }
        replyEntity.update(replyRequestDto);
        return "수정 완료";
    }

    @Transactional
    public String deleteCommentOfParent(Long id,Long coId, HttpServletRequest request ){
        String userEmail = jwtTokenProvider.fetchUserEmailByHttpRequest(request);
        CommentEntity commentEntity = commentRepository.findById(coId).orElseThrow(()->{throw new NotFoundException("해당 댓글이 없습니다.",ErrorCode.NOT_FOUND_EXCEPTION);});

        if (!(commentEntity.getUserEmail().equals(userEmail))){
            throw new ForbiddenException(ErrorCode.FORBIDDEN_EXCEPTION.getMessage(), ErrorCode.FORBIDDEN_EXCEPTION);
        }
        this.deleteBoardCommentNumbering(commentEntity.getBoardEntity().getId(),userEmail);
        //자식이 있으면 deleted == true
        if (!commentEntity.getReplies().isEmpty()){
            commentEntity.delete();
            decreaseBoardCommentNum(id);
            return "임시 삭제완료";
        }else{
            decreaseBoardCommentNum(id);
            commentRepository.delete(commentEntity);
            return "삭제완료";
        }

    }
    private void deleteBoardCommentNumbering(long boardId, String userEmail) {
        boardCommentNumberingRepository.deleteByBoardIdAndUserEmail(boardId,userEmail);

    }
    @Transactional
    public String deleteCommentOfChild(Long id,Long reId,HttpServletRequest request){
        String userEmail = jwtTokenProvider.fetchUserEmailByHttpRequest(request);
        ReplyEntity replyEntity = replyRepository.findById(reId).orElseThrow(()->{throw new NotFoundException("해당 댓글이 없습니다.",ErrorCode.NOT_FOUND_EXCEPTION);});
        if (!(replyEntity.getUserEmail().equals(userEmail))){
            throw new ForbiddenException(ErrorCode.FORBIDDEN_EXCEPTION.getMessage(), ErrorCode.FORBIDDEN_EXCEPTION);
        }

       this.deleteBoardCommentNumbering(replyEntity.getParent().getBoardEntity().getId(),userEmail);

        decreaseBoardCommentNum(id);
        if (replyEntity.getParent().getDeleted()){
            CommentEntity commentEntity = replyEntity.getParent();
            replyRepository.delete(replyEntity);
            if (commentEntity.getReplies().size() == 1 && commentEntity.getReplies().get(0).getId()==replyEntity.getId()){
                commentRepository.delete(commentEntity);
            }
            return "삭제완료";
        }
        replyRepository.delete(replyEntity);


        return "삭제완료";
    }
    public void decreaseBoardCommentNum(Long id){
        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow(()->{throw new NotFoundException("해당 게시글이 없습니다.",ErrorCode.NOT_FOUND_EXCEPTION);});
        //보드 게시판의 댓글수 업데이트
        if(boardEntity.getCommentNum() > 0) {
            boardEntity.Update(boardEntity.getCommentNum() - 1);
        }
    }
}
