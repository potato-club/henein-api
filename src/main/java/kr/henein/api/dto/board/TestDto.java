package kr.henein.api.dto.board;

import lombok.Getter;
import org.springframework.stereotype.Service;

import javax.persistence.Lob;

@Getter
@Service
public class TestDto {
    @Lob
    private String title;
    private String text;
}
