package com.example.demo.entity;

import com.example.demo.dto.comment.CommentRequestDto;
import com.example.demo.enumCustom.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Builder
public class CommentEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private BoardEntity boardEntity;

    @OneToMany(mappedBy = "parent")
    private List<ReplyEntity> replies;
    @Column(nullable = false)
    private Boolean deleted = false;
    @Column(nullable = false)
    private Boolean updated;

    public void update(CommentRequestDto commentRequestDto) {
        this.comment = commentRequestDto.getComment();
    }
    public void delete(){
        this.deleted = true;
        this.userName = "알 수 없음";
        this.comment = "삭제된 댓글입니다.";
    }
}
