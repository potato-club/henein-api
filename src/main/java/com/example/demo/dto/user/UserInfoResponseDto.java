package com.example.demo.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoResponseDto {
    private String userName;
    private String uid;
    private String pickCharacter;
    private String imageUrl;

    public UserInfoResponseDto(String userName, String uid, String pickCharacter, String imageUrl) {
        this.userName = userName;
        this.uid = uid;
        this.pickCharacter = pickCharacter;
        this.imageUrl = imageUrl;
    }
    public void UpdateImage () {

    }
}
