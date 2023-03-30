package com.example.demo.dto.board;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RecommendDTO {
    private int recommend;
    public RecommendDTO(int recommend){
        this.recommend =recommend;
    }
}
