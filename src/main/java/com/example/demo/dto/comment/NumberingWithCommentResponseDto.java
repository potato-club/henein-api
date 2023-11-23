package com.example.demo.dto.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class NumberingWithCommentResponseDto {

    private List<NumberingResponseDto> writerList;
    private List<CommentResponseDto> commentList;

}
