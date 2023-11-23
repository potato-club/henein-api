package com.example.demo.entity;

import com.example.demo.enumCustom.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "numbering")
public class BoardCommentNumberingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private BoardEntity boardEntity;
    @OneToMany(mappedBy = "numberingEntity",orphanRemoval = true)
    private List<CommentEntity> commentEntityList;
    @OneToMany(mappedBy = "numberingEntity",orphanRemoval = true)
    private List<ReplyEntity> replyEntityList;

    @Column(nullable = false)
    private int connectionCount;
    @Column(nullable = false)
    private String userEmail;
    @Column(nullable = false)
    private String nickName;
    @Column(nullable = false)
    private String userUid;
    @Column(nullable = false)
    private UserRole role;

    public void updateConnectionCount(int num) { // -1 or 1
        this.connectionCount += num;
    }

}
