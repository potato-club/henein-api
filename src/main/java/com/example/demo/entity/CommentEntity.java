package com.example.demo.entity;

import com.example.demo.dto.comment.CommentRequsetDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
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
    private String userName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private BoardEntity boardEntity;

    @OneToMany(mappedBy = "parent")
    private List<ReplyEntity> replies = new ArrayList<>();
    @Column(nullable = false)
    private Boolean updated;

    public void update(CommentRequsetDto commentRequsetDto, String userName) {
        this.comment = commentRequsetDto.getComment();
        this.userName = userName;
    }
}
