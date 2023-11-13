package com.example.demo.dto.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class NumberingWithCommentResponseDto {

    private Map<String, Integer> numbering;
    private List<CommentResponseDto> commentList;

    public NumberingWithCommentResponseDto(Map<String,Integer> numbering, List<CommentResponseDto> commentList) {
        this.numbering = numbering;
        this.commentList = commentList;
    }
}
