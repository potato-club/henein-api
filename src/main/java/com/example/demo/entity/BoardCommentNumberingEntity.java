package com.example.demo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class BoardCommentNumberingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private long boardId;

    @Column(nullable = false)
    private String userEmail;

    @Column(nullable = false)
    private int userNumbering;

    public BoardCommentNumberingEntity (long boardId, String userEmail,int userCount) {
        this.boardId = boardId;
        this.userEmail = userEmail;
        this.userNumbering = userCount+1;
    }

}
