package com.example.demo.entity;

import com.example.demo.dto.comment.ReplyRequestDto;
import com.example.demo.enumCustom.UserRole;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Builder
public class ReplyEntity extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String tag;
    @Column(nullable = false)
    private String comment;
    @Column(nullable = false)
    private String userEmail;
    @Column(nullable = false)
    private UserRole roleInBoard;
    @Column(nullable = false)
    private String userName;
    @Column(nullable = false)
    private String uid;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id",nullable = false)
    private CommentEntity parent;
    @Column(nullable = false)
    private Boolean updated;
    public void update(ReplyRequestDto replyRequestDto, String userName) {
        this.tag = replyRequestDto.getTag();
        this.comment = replyRequestDto.getComment();
        this.updated = true;
        this.userName = userName;
    }
}
