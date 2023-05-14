package com.example.demo.dto.board;

import com.example.demo.enumCustom.BoardType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class BoardRequestDto {
    @ApiModelProperty(value="게시글 제목", example = "테스트 제목입니다.", required = true)
    private String title;
    @ApiModelProperty(value="게시글 내용", example = "테스트 글입니다.", required = true)
    private String text;
    @ApiModelProperty(value = "게시판 유형", example = "E,A,B,H,I,N", required = true)
    private String boardType;
}
