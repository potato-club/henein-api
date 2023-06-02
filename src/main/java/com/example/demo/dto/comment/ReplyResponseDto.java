package com.example.demo.dto.comment;

import com.example.demo.entity.ReplyEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplyResponseDto {

    private Long replyId;
    private String tag;
    private String comment;
    private Boolean updated;
    public ReplyResponseDto(ReplyEntity reply){
        replyId = reply.getId();
        tag = reply.getTag();
        comment = reply.getComment();
        updated = reply.getUpdated();
    }
}
