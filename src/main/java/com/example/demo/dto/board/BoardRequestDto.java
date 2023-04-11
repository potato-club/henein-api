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
    Long id;
    @ApiModelProperty(value="수정 시간", example = "yyyy-MM-dd HH:mm", hidden = true)
    private LocalDateTime modifiedDate = LocalDateTime.now();
    @ApiModelProperty(value="게시글 제목", example = "테스트 제목입니다.", required = true)
    private String title;
    @ApiModelProperty(value="사용자 정보", hidden = true)
    private String nickname;
    @ApiModelProperty(value="게시글 내용", example = "테스트 글입니다.", required = true)
    private String text;
    private String boardType;
}
