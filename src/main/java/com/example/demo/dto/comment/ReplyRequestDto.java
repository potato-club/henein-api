package com.example.demo.dto.comment;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class ReplyRequestDto {
    @ApiModelProperty(value="작성될 게시글 Id", example = "댓글 post시 사용", required = true)
    private Long boardId;
    @ApiModelProperty(value="부모댓글 id post 요청시에만 사용 ", example = "대댓글일때 사용", required = true)
    private Long commentId;
    @ApiModelProperty(value="대댓글 수정,삭제시 사용", example = "대댓글일때 사용", required = true)
    private Long replyId;
    @ApiModelProperty(value="태그", example = "대댓글에 대한 태그남길때 사용", required = true)
    private String tag;
    @ApiModelProperty(value="댓글 내용", example = "문자열", required = true)
    private String comment;
}
