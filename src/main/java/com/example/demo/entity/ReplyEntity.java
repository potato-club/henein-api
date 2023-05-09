package com.example.demo.entity;

import com.example.demo.dto.comment.CommentRequsetDto;
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
    private String userId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id",nullable = false)
    private CommentEntity parent;
    @Column(nullable = false)
    private Boolean updated;
    public void update(CommentRequsetDto commentRequsetDto) {
        this.comment = commentRequsetDto.getComment();
    }
}
