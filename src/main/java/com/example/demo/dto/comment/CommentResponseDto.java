package com.example.demo.dto.comment;

import com.example.demo.entity.CommentEntity;
import com.example.demo.entity.ReplyEntity;
import com.example.demo.enumCustom.UserRole;
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
    private UserRole roleInBoard;
    private String uid;
    private String comment;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime modifiedDate;
    private List<ReplyResponseDto> replies;

    public CommentResponseDto(CommentEntity commentEntity){
        this.commentId = commentEntity.getId();
        this.roleInBoard = commentEntity.getRoleInBoard();
        this.userName = commentEntity.getUserName();
        this.comment = commentEntity.getComment();
        this.modifiedDate = commentEntity.getModifiedDate();

    }
    public void setUid(String uid){
        this.uid = uid;
    }

    public void setReplies(List<ReplyResponseDto> replies){
        this.replies = replies;
    }
}
