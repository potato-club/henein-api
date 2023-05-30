package com.example.demo.dto.board;

import lombok.Getter;
import org.springframework.stereotype.Service;

@Getter
@Service
public class TestDto {
    private String title;
    private String text;
}
