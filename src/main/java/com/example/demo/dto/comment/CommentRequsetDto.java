package com.example.demo.dto.comment;

import lombok.Getter;

@Getter
public class CommentRequsetDto {
    private Long boardId;
    private Long commentId;
    private String userId;
    private String comment;

}
