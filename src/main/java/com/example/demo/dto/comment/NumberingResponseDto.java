package com.example.demo.dto.comment;

import com.example.demo.entity.BoardCommentNumberingEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NumberingResponseDto {
    private String uid;
    private String nickName;
    private String role;

    public NumberingResponseDto (BoardCommentNumberingEntity boardCommentNumberingEntity) {
        this.nickName = boardCommentNumberingEntity.getNickName();
        this.role = boardCommentNumberingEntity.getRole().toString();
    }
    public NumberingResponseDto (BoardCommentNumberingEntity boardCommentNumberingEntity, String userEmail) {
        if (boardCommentNumberingEntity.getUserEmail().equals(userEmail))
            this.uid = boardCommentNumberingEntity.getUserUid();
        this.nickName = boardCommentNumberingEntity.getNickName();
        this.role = boardCommentNumberingEntity.getRole().toString();
    }
}
