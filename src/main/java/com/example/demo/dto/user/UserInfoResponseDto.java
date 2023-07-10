package com.example.demo.dto.user;

import com.example.demo.entity.UserEntity;
import lombok.Getter;

@Getter
public class UserInfoResponseDto {
    private String userName;


    public UserInfoResponseDto(UserEntity userEntity){
        this.userName = userEntity.getUserName();

    }
}
