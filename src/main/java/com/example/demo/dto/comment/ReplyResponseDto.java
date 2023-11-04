package com.example.demo.dto.comment;

import com.example.demo.entity.ReplyEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
public class ReplyResponseDto {

    private Long replyId;
    private String userName;
    private String uid;
    private String tag;
    private String comment;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime modifiedDate;
    private Boolean updated;
    public ReplyResponseDto(ReplyEntity reply, String uid){
        replyId = reply.getId();
        userName =reply.getUserName();
        tag = reply.getTag();
        comment = reply.getComment();
        modifiedDate = reply.getModifiedDate();
        updated = reply.getUpdated();
        if (Objects.equals(uid, reply.getUid())) {
            this.uid = reply.getUid();
        }
    }
    public ReplyResponseDto(ReplyEntity reply) {
        replyId = reply.getId();
        userName =reply.getUserName();
        tag = reply.getTag();
        comment = reply.getComment();
        modifiedDate = reply.getModifiedDate();
        updated = reply.getUpdated();
    }
}
