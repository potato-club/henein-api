package com.example.demo.dto.login;

import lombok.Getter;

@Getter
public class BasicLoginRequestDto {
    //@ApiModelProperty(value="유저 고유 email", example = "~@naver.com", required = true)
    private String userEmail;
    //@ApiModelProperty(value="유저 비밀번호", example = "문자열", required = true)
    private String password;

}
