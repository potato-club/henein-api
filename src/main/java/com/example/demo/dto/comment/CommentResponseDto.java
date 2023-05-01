package com.example.demo.dto.comment;

import com.example.demo.entity.CommentEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CommentResponseDto {
    private Long commentId;
    private String userId;
    private String comment;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime modifiedDate;
    private List<CommentResponseDto> replies;

    public CommentResponseDto(CommentEntity commentEntity){
        this.commentId = commentEntity.getId();
        this.userId = commentEntity.getUserId();
        this.comment = commentEntity.getComment();
        this.modifiedDate = commentEntity.getModifiedDate();
    }
    public void setReplies(List<CommentResponseDto> replies){
        this.replies = replies;
    }
}
