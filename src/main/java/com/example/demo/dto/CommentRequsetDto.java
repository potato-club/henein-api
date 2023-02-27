package com.example.demo.dto;

import com.example.demo.entity.BoardEntity;
import com.example.demo.entity.CommentEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
public class CommentRequsetDto {
    private String userName;
    private String text;

}
