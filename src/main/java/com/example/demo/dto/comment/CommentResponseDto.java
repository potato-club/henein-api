package com.example.demo.dto.comment;

import com.example.demo.entity.CommentEntity;
import com.example.demo.entity.ReplyEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class CommentResponseDto {
    private Long commentId;
    private String userName;
    private String uid;
    private String comment;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime modifiedDate;
    private List<ReplyResponseDto> replies;

    public CommentResponseDto(CommentEntity commentEntity,String uid){
        this.commentId = commentEntity.getId();
        this.userName = commentEntity.getUserName();
        this.comment = commentEntity.getComment();
        this.modifiedDate = commentEntity.getModifiedDate();
        if (Objects.equals(uid, commentEntity.getUid())) {
            this.uid = commentEntity.getUid();
        }
    }
    public CommentResponseDto(CommentEntity commentEntity){
        this.commentId = commentEntity.getId();
        this.userName = commentEntity.getUserName();
        this.comment = commentEntity.getComment();
        this.modifiedDate = commentEntity.getModifiedDate();

    }

    public void setReplies(List<ReplyResponseDto> replies){
        this.replies = replies;
    }
}
