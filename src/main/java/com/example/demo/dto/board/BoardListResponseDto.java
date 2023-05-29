package com.example.demo.dto.board;

import com.example.demo.dto.file.FileResponseDto;
import com.example.demo.entity.BoardEntity;
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
public class BoardListResponseDto {
    private Long id;
    @ApiModelProperty(value="게시글 제목", example = "테스트 제목입니다.", required = true)
    private String title;
    @ApiModelProperty(value="댓글 갯수", example = "정수값", required = true)
    private int commentNum;
    @ApiModelProperty(value="게시글 작성자", example = "테스트 글쓴이", required = true)
    private String userName;
    @ApiModelProperty(value = "작성자 추가정보", example = "50층")
    private String userFloor;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime createTime;
    @ApiModelProperty(value="조회수", example = "정수값", required = true)
    private int views;

    public BoardListResponseDto(BoardEntity boardEntity){
        this.id = boardEntity.getId();
        this.title = boardEntity.getTitle();
        this.commentNum = boardEntity.getCommentNum();
        this.userName = boardEntity.getUserEntity().getUserName();
        this.userFloor = boardEntity.getUserEntity().getFloor();
        this.createTime = boardEntity.getCreatedDate();
        this.views = boardEntity.getViews();
    }
}