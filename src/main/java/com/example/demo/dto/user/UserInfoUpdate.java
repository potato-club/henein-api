package com.example.demo.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class UserInfoUpdate {
    @ApiModelProperty(example = "이미지 파일")
    private MultipartFile image;
    @ApiModelProperty(example = "유저 이름")
    private String userName;
}
