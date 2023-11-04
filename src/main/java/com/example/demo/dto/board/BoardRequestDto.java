package com.example.demo.dto.board;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.Lob;


@Getter
@Setter
public class BoardRequestDto {
    @Lob
    private String title;
    private String text;
    private String boardType;
    //private List<MultipartFile> images;

}
