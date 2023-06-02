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
    @ApiModelProperty(value="작성될 게시글 Id", example = "댓글 post시 사용", required = true)
    private Long boardId;
    @ApiModelProperty(value="댓글 수정,삭제 시 사용", example = "댓글 수정,삭제 시 사용", required = true)
    private Long commentId;
    @ApiModelProperty(value="댓글 내용", example = "문자열", required = true)
    private String comment;

}
