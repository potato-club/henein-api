package com.example.demo.dto.board;

import com.example.demo.dto.file.FileResponseDto;
import com.example.demo.entity.*;
import com.example.demo.enumCustom.BoardType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class BoardResponseDto {
    private Long id;
    private BoardType boardType;
    private String title;
    private int commentNum;
    private String nickname; //이름 바꿔야함
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime createTime;
    private int views;
    private int recommend;
    private String text;
    private List<FileResponseDto> image;

    public BoardResponseDto (BoardEntity boardEntity){
        this.id = boardEntity.getId();
        this.boardType =boardEntity.getBoardType();
        this.title = boardEntity.getTitle();
        this.commentNum = boardEntity.getCommentNum();
        this.nickname = boardEntity.getNickname();
        this.createTime = boardEntity.getCreateTime();
        this.views = boardEntity.getViews();
        this.recommend = boardEntity.getRecommend();
        this.text = boardEntity.getText();
        this.image = boardEntity.getImage().stream().map(FileResponseDto::new).collect(Collectors.toList());
    }
}