package kr.henein.api.dto.board;


import lombok.Getter;
import lombok.Setter;


import javax.persistence.Lob;


@Getter
@Setter
public class BoardRequestDto {
    private String title;
    @Lob
    private String text;
    private String boardType;
    //private List<MultipartFile> images;

}
