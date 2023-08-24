package com.example.demo.entity;

import com.example.demo.dto.user.UserNicknameChange;
import com.example.demo.enumCustom.UserRole;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserEntity extends BaseTimeEntity{
    @Id
    private String uid;
    @Column(nullable = false)
    private UserRole userRole;
    @Column(unique = true,nullable = false)
    private String userEmail;
    @Column(nullable = false)
    private String userName;
    @Column
    private String password;

    @Column(length = 512,nullable = false)
    private String refreshToken;


    public UserEntity(String email) {
        this.userEmail = email;
        this.userName = "ㅇㅇ";
        this.userRole = UserRole.USER;
        this.uid = String.valueOf(UUID.randomUUID());
    }
    public void Update(UserNicknameChange userNicknameChange) {
        this.userName = userNicknameChange.getUserName();
    }

    public void setRefreshToken(String refreshToken){

        this.refreshToken = refreshToken;
    }

}
