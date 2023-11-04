package com.example.demo.entity;

import com.example.demo.dto.user.UserInfoUpdate;
import com.example.demo.enumCustom.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserEntity extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
    public void Update(String userName) {
        this.userName = userName;
    }
    public void UpdatePW(String password) {this.password = password;}

    public void setRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }

}
