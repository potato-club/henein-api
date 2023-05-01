package com.example.demo.entity;

import com.example.demo.dto.board.BoardRecommendDTO;
import com.example.demo.dto.board.BoardRequestDto;
import com.example.demo.dto.comment.CommentNumUpdateDto;
import com.example.demo.dto.board.ViewIncreaseDto;
import com.example.demo.enumCustom.BoardType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "board")
public class BoardEntity extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column
    private BoardType boardType;
    @Column(nullable = false)
    private String title;
    @Column
    private int commentNum;
    @Column
    private String nickname;
    @Column
    private int views;
    @Column
    private int recommend;
    @Column
    private String text;

    @OneToMany(mappedBy = "boardEntity", orphanRemoval = true)
    private List<S3File> image = new ArrayList<>();

    @OneToMany(mappedBy = "boardEntity", orphanRemoval = true)
    private List<CommentEntity> commentEntityList = new ArrayList<>();

    @Builder
    public BoardEntity (String title, String nickname,String text, BoardType boardType){
        this.title = title;
        this.nickname = nickname;
        this.text = text;
        this.boardType = boardType;
    }
    public void Update(BoardRequestDto boardRequestDto){
        this.title = boardRequestDto.getTitle();
        this.nickname = boardRequestDto.getNickname();
        this.text = boardRequestDto.getText();
    }
    public void Update(BoardRecommendDTO boardRecommendDTO){
        this.recommend = boardRecommendDTO.getRecommend();
    }
    public void Update(CommentNumUpdateDto commentNumUpdateDto){
        this.commentNum = commentNumUpdateDto.getCommentNum();
    }
    public void Update(ViewIncreaseDto viewIncreaseDto){ this.views =viewIncreaseDto.getViews();}
}
