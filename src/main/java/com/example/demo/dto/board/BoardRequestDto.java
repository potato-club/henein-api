package com.example.demo.dto.board;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardRequestDto {
    private String title;
    private String name;
    private String text;
    private String boardType;
}
