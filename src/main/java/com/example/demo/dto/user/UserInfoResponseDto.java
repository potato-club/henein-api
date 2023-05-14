package com.example.demo.dto.user;

import com.example.demo.entity.UserEntity;
import lombok.Getter;

@Getter
public class UserInfoResponseDto {
    private String userName;
    private String floor;
    private int userLevel;
    private String job;

    public UserInfoResponseDto(UserEntity userEntity){
        this.userName = userEntity.getUserName();
        this.floor = userEntity.getFloor();
        this.userLevel =userEntity.getUserLevel();
        this.job = userEntity.getJob();
    }
}
