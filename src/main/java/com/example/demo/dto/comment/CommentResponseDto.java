package com.example.demo.dto.comment;

import com.example.demo.entity.CommentEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
public class CommentResponseDto {
    private Long id;
    private Integer writerId;
    private String comment;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime modifiedDate;
    private List<ReplyResponseDto> replies;

    public CommentResponseDto(CommentEntity commentEntity){
        this.id = commentEntity.getId();
        this.comment = commentEntity.getComment();
        this.modifiedDate = commentEntity.getModifiedDate();
        this.replies = new ArrayList<>();
    }
    public void setWriterId(int writerId) {
        this.writerId = writerId;
    }

    public void setReplies(List<ReplyResponseDto> replies){
        this.replies = replies;
    }
}
