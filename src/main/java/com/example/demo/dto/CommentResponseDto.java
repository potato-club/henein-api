package com.example.demo.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private long commentId;
    private String userId;
    private String body;
    private LocalDateTime createTime;
}
