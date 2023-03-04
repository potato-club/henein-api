package com.example.demo.entity;

import com.example.demo.dto.BoardRequestDto;
import com.example.demo.dto.CommentNumUpdateDto;
import com.example.demo.dto.RecommandUpdateDTO;
import com.example.demo.enumCustom.BoardType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name= "board")
public class BoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column
    private BoardType boardType;
    @Column
    private String title;
    @Column
    private int commentNum;
    @Column
    private String name;
    @Column
    private LocalDateTime createTime;
    @Column
    private int views;
    @Column
    private int recommend;
    @Column
    private String text;

    public void Update(BoardRequestDto boardRequestDto){
        this.title = boardRequestDto.getTitle();
        this.name = boardRequestDto.getName();
        this.createTime = LocalDateTime.now();
        this.text = boardRequestDto.getText();
    }
    public void Update(RecommandUpdateDTO recommandUpdateDTO){
        this.recommend = recommandUpdateDTO.getRecommend();
    }
    public void Update(CommentNumUpdateDto commentNumUpdateDto){
        this.commentNum = commentNumUpdateDto.getCommentNum();
    }
}
