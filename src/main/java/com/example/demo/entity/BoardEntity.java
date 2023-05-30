package com.example.demo.entity;

import com.example.demo.dto.board.BoardRecommendDTO;
import com.example.demo.dto.board.BoardRequestDto;
import com.example.demo.dto.board.TestDto;
import com.example.demo.dto.comment.CommentNumUpdateDto;
import com.example.demo.dto.board.ViewIncreaseDto;
import com.example.demo.enumCustom.BoardType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.userdetails.User;

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
    @ManyToOne
    @JoinColumn(name ="user", nullable = false)
    private UserEntity userEntity;
    @Column
    private int views;
    @Column
    private int recommend;
    @Column(nullable = false)
    private String text;

    @OneToMany(mappedBy = "boardEntity", orphanRemoval = true)
    private List<S3File> image = new ArrayList<>();

    @OneToMany(mappedBy = "boardEntity", orphanRemoval = true)
    private List<CommentEntity> commentEntityList = new ArrayList<>();

    @Builder
    public BoardEntity (BoardRequestDto boardRequestDto, BoardType board, UserEntity userEntity){
        this.title = boardRequestDto.getTitle();
        this.userEntity = userEntity;
        this.text = boardRequestDto.getText();
        this.boardType = board;
    }
    public void Update(TestDto testDto){
        this.title = testDto.getTitle();
        this.text = testDto.getText();
    }
    public void Update(BoardRecommendDTO boardRecommendDTO){
        this.recommend = boardRecommendDTO.getRecommend();
    }
    public void Update(CommentNumUpdateDto commentNumUpdateDto){
        this.commentNum = commentNumUpdateDto.getCommentNum();
    }
    public void Update(ViewIncreaseDto viewIncreaseDto){ this.views =viewIncreaseDto.getViews();}
}
