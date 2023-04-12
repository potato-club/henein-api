package com.example.demo.dto.user;

import com.example.demo.entity.UserEntity;
import lombok.Getter;

@Getter
public class UserInfoResponseDto {
    private String username;
    private String floor;
    private int userLevel;
    private String job;

    public UserInfoResponseDto(UserEntity userEntity){
        this.username = userEntity.getUsername();
        this.floor = userEntity.getFloor();
        this.userLevel =userEntity.getUserLevel();
        this.job = userEntity.getJob();
    }
}
