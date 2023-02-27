package com.example.demo.entity;

import com.example.demo.dto.CommentRequsetDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "comments")
@Entity
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "BoardId")
    private BoardEntity boardEntity;
    @Column
    private String userName;
    @Column
    private String text;

    public void Update(CommentRequsetDto commentRequsetDto){
        this.userName = commentRequsetDto.getUserName();
        this.text = commentRequsetDto.getText();
    }
}
