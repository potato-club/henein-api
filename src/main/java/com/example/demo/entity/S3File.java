package com.example.demo.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class S3File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_url")
    private String fileUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "board_entity_id")
    private BoardEntity boardEntity;

    @Column(name = "creation_time")
    private LocalDateTime creationTime;

    @Builder
    public S3File(String fileName, String fileUrl, BoardEntity boardEntity){
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.boardEntity = boardEntity;
    }
}
