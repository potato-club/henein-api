package com.example.demo.entity;

import com.example.demo.enumCustom.S3EntityType;
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
public class S3File extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_url")
    private String fileUrl;

    @Enumerated(EnumType.STRING)
    @Column
    private S3EntityType s3EntityType; //게시글의 사진인지 유저의 사진인지 분류
    @Column
    private Long typeId; //게시글이면 게시글의 id

    @Column
    private String email;


    @Builder
    public S3File(String fileName, String fileUrl){
        this.fileName = fileName;
        this.fileUrl = fileUrl;
    }
    public void setEntityData(S3EntityType s3EntityType,Long typeId) {
        this.s3EntityType = s3EntityType;
        this.typeId = typeId;
    }
}
