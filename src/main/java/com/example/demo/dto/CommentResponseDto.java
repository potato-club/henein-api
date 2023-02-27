package com.example.demo.dto;

import com.example.demo.entity.CommentEntity;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private long commentId;
    private String userName;
    private String text;

    //entity -> dto
    public CommentResponseDto(CommentEntity commentEntity){
        this.commentId = commentEntity.getId();
        this.userName = commentEntity.getUserName();
        this.text = commentEntity.getText();
    }
}
