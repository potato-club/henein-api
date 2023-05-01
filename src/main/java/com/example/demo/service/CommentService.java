package com.example.demo.service;

import com.example.demo.dto.comment.CommentRequsetDto;
import com.example.demo.dto.comment.CommentResponseDto;
import com.example.demo.dto.comment.CommentNumUpdateDto;
import com.example.demo.entity.BoardEntity;
import com.example.demo.entity.CommentEntity;
import com.example.demo.entity.QBoardEntity;
import com.example.demo.entity.QCommentEntity;
import com.example.demo.error.ErrorCode;
import com.example.demo.error.Exception.NotFoundException;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.CommentRepository;
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
    final private BoardRepository boardRepository;
    final private JPAQueryFactory jpaQueryFactory;

    @Transactional
    public List<CommentResponseDto> getCommentOfBoard(Long boardId){
        if (!boardRepository.existsById(boardId)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION,ErrorCode.NOT_FOUND_EXCEPTION.getMessage());
        }
        QCommentEntity qCommentEntity = QCommentEntity.commentEntity;
        QBoardEntity qBoardEntity = QBoardEntity.boardEntity;

        List<CommentEntity> result = jpaQueryFactory.select(qCommentEntity)
                .from(qCommentEntity)
                .innerJoin(qCommentEntity.boardEntity,qBoardEntity)
                .where(qCommentEntity.boardEntity.id.eq(boardId).and(qCommentEntity.parent.isNull())) //부모 댓글만 먼저 싹 가져옴
                .orderBy(qCommentEntity.id.asc())
                .fetch();

        List<CommentResponseDto> responseDtoList = new ArrayList<>();
        for (CommentEntity parentComment : result) {
            List<CommentEntity> child = getChildComments(parentComment);
            CommentResponseDto parentDto = new CommentResponseDto(parentComment);
            parentDto.setReplies(child.stream().map(CommentResponseDto::new).collect(Collectors.toList()));
            responseDtoList.add(parentDto);
        }
        return responseDtoList;

    }
    private List<CommentEntity> getChildComments(CommentEntity parentEntity){
        QCommentEntity qCommentEntity = QCommentEntity.commentEntity;
        return jpaQueryFactory.select(qCommentEntity)
                .from(qCommentEntity)
                .where(qCommentEntity.parent.eq(parentEntity))
                .orderBy(qCommentEntity.id.asc())
                .fetch();
    }

    @Transactional
    public String addCommentOfParent(CommentRequsetDto commentRequsetDto, HttpServletRequest request ){
        BoardEntity boardEntity = boardRepository.findById(commentRequsetDto.getBoardId()).orElseThrow(()->{throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION,ErrorCode.NOT_FOUND_EXCEPTION.getMessage());});
        CommentEntity commentEntity = createComment(boardEntity,commentRequsetDto);
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

        CommentEntity childComment = createComment(parentComment.getBoardEntity(),commentRequsetDto);
        commentRepository.save(childComment);
        parentComment.addReply(childComment);

        //보드 게시판의 댓글수 업데이트
        CommentNumUpdateDto commentNumUpdateDto = new CommentNumUpdateDto();
        commentNumUpdateDto.setCommentNum(boardEntity.getCommentNum()+1);
        boardEntity.Update(commentNumUpdateDto);

        boardRepository.save(boardEntity);
        return "대댓글 작성 완료";
    }

    @Transactional
    public String updateCommentOfId(CommentRequsetDto commentRequsetDto, HttpServletRequest request){
        CommentEntity commentEntity = commentRepository.findById(commentRequsetDto.getCommentId()).orElseThrow(()->{throw new RuntimeException("해당 댓글이 없습니다");});
        commentEntity.update(commentRequsetDto);
        commentRepository.save(commentEntity);
        return "수정 완료";
    }

    @Transactional
    public String deleteComment(CommentRequsetDto commentRequsetDto, HttpServletRequest request){
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
    private CommentEntity createComment(BoardEntity boardEntity,CommentRequsetDto commentRequsetDto){
        CommentEntity commentEntity = CommentEntity.builder()
                .boardEntity(boardEntity)
                .comment(commentRequsetDto.getComment())
                .updated(false)
                .userId(commentRequsetDto.getUserId())
                .build();
        return commentEntity;
    }
}
