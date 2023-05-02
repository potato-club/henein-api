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
@Table(name = "comments")
public class CommentEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String comment;
    @Column(nullable = false)
    private String userId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private BoardEntity boardEntity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private CommentEntity parent;
    @Builder.Default
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<CommentEntity> replies = new ArrayList<>();
    @Column(nullable = false)
    private Boolean updated;

//    public boolean isParent(){
//        return this.parent == null;
//    }
    public void addReply(CommentEntity commentEntity){
        this.replies.add(commentEntity);
        commentEntity.setParent(this);
    }
    private void setParent(CommentEntity parentComment){
        this.parent = parentComment;
    }
    public void update (CommentRequsetDto commentRequsetDto){
        this.comment = commentRequsetDto.getComment();
        this.updated = true;
    }

    public void setReplies(List<CommentEntity> grandchildComments) {
        this.replies = replies;
    }
}
