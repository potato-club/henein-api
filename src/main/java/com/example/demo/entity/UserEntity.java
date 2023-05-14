package com.example.demo.entity;

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

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserEntity extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private UserRole userRole;
    @Column(unique = true,nullable = false)
    private String userEmail;
    @Column(unique = true)
    @Size(min=5,max=50)
    private String userName;
    @Column
    private String floor;
    @Column
    private int userLevel;
    @Column
    private String  job;
    @Column
    private String password;

    @Column(length = 512)
    private String refreshToken;


    public UserEntity(String email, int guestCount, UserRole userRole) {
        this.userEmail =email;
        this.userName = "guest"+guestCount;
        this.userRole = userRole;
    }
    public void Update(String username) {
        this.userName = username;
    }

    public void setRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }

}
