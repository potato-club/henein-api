package com.example.demo.dto;

import com.example.demo.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@NoArgsConstructor
@Setter
public class UserRegisterRequest {
    private String username;
    private String password;
    private String email;

    public UserEntity toEntity(UserRegisterRequest userRegisterRequest){
        return UserEntity.builder()
                .username(userRegisterRequest.username)
                .password(userRegisterRequest.password)
                .email(userRegisterRequest.email)
                .build();
    }
}
