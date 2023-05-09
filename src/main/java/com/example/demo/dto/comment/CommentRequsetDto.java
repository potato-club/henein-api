package com.example.demo.dto.comment;

import lombok.Getter;

@Getter
public class CommentRequsetDto {
    private Long boardId;
    private Long parentCommentId;
    private Long childCommentId;
    private String tag;
    private String userId;
    private String comment;

}
