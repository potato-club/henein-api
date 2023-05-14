package com.example.demo.dto.comment;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequsetDto {
    @ApiModelProperty(value="작성될 게시글 Id", example = "필수적요소", required = true)
    private Long boardId;
    @ApiModelProperty(value="대댓글일시", example = "대댓글일때 사용", required = true)
    private Long commentId;
    @ApiModelProperty(value="태그", example = "대댓글에 대한 태그남길때 사용", required = true)
    private String tag;
    @ApiModelProperty(value="댓글 내용", example = "문자열", required = true)
    private String comment;

}
