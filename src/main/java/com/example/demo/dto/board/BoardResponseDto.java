package com.example.demo.dto.board;

import com.example.demo.dto.file.FileResponseDto;
import com.example.demo.dto.user.UserInfoResponseDto;
import com.example.demo.entity.*;
import com.example.demo.enumCustom.BoardType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class BoardResponseDto {
    private Long id;
    @ApiModelProperty(value="게시글 타입", example = "Advertise", required = true)
    private BoardType boardType;
    @ApiModelProperty(value="게시글 제목", example = "테스트 제목입니다.", required = true)
    private String title;
    @ApiModelProperty(value="댓글 갯수", example = "정수값", required = true)
    private int commentNum;
    @ApiModelProperty(value="게시글 작성자", example = "테스트 글쓴이", required = true)
    private UserInfoResponseDto userInfoResponseDto;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime createTime;
    @ApiModelProperty(value="조회수", example = "정수값", required = true)
    private int views;
    @ApiModelProperty(value="추천받은 수", example = "정수값", required = true)
    private int recommend;
    @ApiModelProperty(value="게시글 내용", example = "테스트 내용입니다~~", required = true)
    private String text;
    @ApiModelProperty(value="첨부된 이미지들", example = "URL 링크로 날라감", required = false)
    private List<FileResponseDto> image;
    @ApiModelProperty(value = "추천했는지", example = "T or F")
    private boolean recommended = false;

    public BoardResponseDto (BoardEntity boardEntity){
        this.id = boardEntity.getId();
        this.boardType =boardEntity.getBoardType();
        this.title = boardEntity.getTitle();
        this.commentNum = boardEntity.getCommentNum();
        this.userInfoResponseDto = new UserInfoResponseDto(boardEntity.getUserEntity());
        this.createTime = boardEntity.getCreatedDate();
        this.views = boardEntity.getViews();
        this.recommend = boardEntity.getRecommend();
        this.text = boardEntity.getText();
        this.image = boardEntity.getImage().stream().map(FileResponseDto::new).collect(Collectors.toList());
    }
    public BoardResponseDto (BoardEntity boardEntity, boolean recommended){
        this.id = boardEntity.getId();
        this.boardType =boardEntity.getBoardType();
        this.title = boardEntity.getTitle();
        this.commentNum = boardEntity.getCommentNum();
        this.userInfoResponseDto = new UserInfoResponseDto(boardEntity.getUserEntity());
        this.createTime = boardEntity.getCreatedDate();
        this.views = boardEntity.getViews();
        this.recommend = boardEntity.getRecommend();
        this.text = boardEntity.getText();
        this.image = boardEntity.getImage().stream().map(FileResponseDto::new).collect(Collectors.toList());
        this.recommended = recommended;
    }
}