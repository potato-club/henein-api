package com.example.demo.entity;

import com.example.demo.enumCustom.UserRole;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    @Column
    private String password;
    @Column(unique = true,nullable = false)
    private String email;
    @Column(length = 512)
    private String refreshToken;

    public UserEntity(String email, int guestCount) {
        this.email =email;
        String username = "guest"+guestCount;
        this.username = username;
    }

    /*public void KakaoSignUp(String email,String refreshToken){
        this.email = email;
        this.refreshToken = refreshToken;
    }*/
    public void setRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }

}
