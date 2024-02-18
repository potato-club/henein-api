package kr.henein.api.dto.board;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardRecommendDTO {
    private int recommend;
    public BoardRecommendDTO(int recommend){
        this.recommend =recommend;
    }
}
