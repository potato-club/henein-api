package com.example.demo.dto.comment;

import com.example.demo.entity.CommentEntity;
import com.example.demo.entity.ReplyEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CommentResponseDto {
    private Long commentId;
    private String userName;
    private String tag;
    private String comment;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime modifiedDate;
    private List<CommentResponseDto> replies;

    public CommentResponseDto(CommentEntity commentEntity){
        this.commentId = commentEntity.getId();
        this.userName = commentEntity.getUserName();
        this.comment = commentEntity.getComment();
        this.modifiedDate = commentEntity.getModifiedDate();
    }
    public CommentResponseDto(ReplyEntity replyEntity){
        this.commentId = replyEntity.getId();
        this.userName = replyEntity.getUserName();
        this.tag = replyEntity.getTag();
        this.comment = replyEntity.getComment();
        this.modifiedDate = replyEntity.getModifiedDate();
    }
    public void setReplies(List<CommentResponseDto> replies){
        this.replies = replies;
    }
}
