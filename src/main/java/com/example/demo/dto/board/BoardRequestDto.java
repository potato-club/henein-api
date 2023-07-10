package com.example.demo.dto.board;

import com.example.demo.enumCustom.BoardType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Lob;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
public class BoardRequestDto {
    @Lob
    private String title;
    private String text;
    private String boardType;
    //private List<MultipartFile> images;

}
