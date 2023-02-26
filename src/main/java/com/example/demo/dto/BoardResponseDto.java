package com.example.demo.dto;

import com.example.demo.entity.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BoardResponseDto {
    private Long id;
    private String title;
    private int commentNum;
    private String name;
    private LocalDateTime createTime;
    private int views;
    private int recommend;
    private String text;

    public BoardResponseDto (BoardEntity boardEntity){
        this.id = boardEntity.getId();
        this.title = boardEntity.getTitle();
        this.commentNum = boardEntity.getCommentNum();
        this.name = boardEntity.getName();
        this.createTime = boardEntity.getCreateTime();
        this.views = boardEntity.getViews();
        this.recommend = boardEntity.getRecommend();
        this.text = boardEntity.getText();
    }
}