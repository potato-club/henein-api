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

    public BoardResponseDto (FreeBoardEntity freeBoardEntity){
        this.id = freeBoardEntity.getId();
        this.title = freeBoardEntity.getTitle();
        this.commentNum = freeBoardEntity.getCommentNum();
        this.name = freeBoardEntity.getName();
        this.createTime = freeBoardEntity.getCreateTime();
        this.views = freeBoardEntity.getViews();
        this.recommend = freeBoardEntity.getRecommend();
        this.text = freeBoardEntity.getText();
    }
    public BoardResponseDto (InfoBoardEntity infoBoardEntity){
        this.id = infoBoardEntity.getId();
        this.title = infoBoardEntity.getTitle();
        this.commentNum = infoBoardEntity.getCommentNum();
        this.name = infoBoardEntity.getName();
        this.createTime = infoBoardEntity.getCreateTime();
        this.views = infoBoardEntity.getViews();
        this.recommend = infoBoardEntity.getRecommend();
        this.text = infoBoardEntity.getText();
    }
    public BoardResponseDto (BossBoardEntity bossBoardEntity){
        this.id = bossBoardEntity.getId();
        this.title = bossBoardEntity.getTitle();
        this.commentNum = bossBoardEntity.getCommentNum();
        this.name = bossBoardEntity.getName();
        this.createTime = bossBoardEntity.getCreateTime();
        this.views = bossBoardEntity.getViews();
        this.recommend = bossBoardEntity.getRecommend();
        this.text = bossBoardEntity.getText();
    }
    public BoardResponseDto (AdvertiseBoardEntity advertiseBoardEntity){
        this.id = advertiseBoardEntity.getId();
        this.title = advertiseBoardEntity.getTitle();
        this.commentNum = advertiseBoardEntity.getCommentNum();
        this.name = advertiseBoardEntity.getName();
        this.createTime = advertiseBoardEntity.getCreateTime();
        this.views = advertiseBoardEntity.getViews();
        this.recommend = advertiseBoardEntity.getRecommend();
        this.text = advertiseBoardEntity.getText();
    }
    public BoardResponseDto (HumorBoardEntity humorBoardEntity){
        this.id = humorBoardEntity.getId();
        this.title = humorBoardEntity.getTitle();
        this.commentNum = humorBoardEntity.getCommentNum();
        this.name = humorBoardEntity.getName();
        this.createTime = humorBoardEntity.getCreateTime();
        this.views = humorBoardEntity.getViews();
        this.recommend = humorBoardEntity.getRecommend();
        this.text = humorBoardEntity.getText();
    }
}