package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
public class CommentRequsetDto {
    private Long commentId;
    private String text;

}
