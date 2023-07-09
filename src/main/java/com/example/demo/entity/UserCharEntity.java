package com.example.demo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class UserCharEntity extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name ="user", nullable = false)
    UserEntity userEntity;
    @Column(nullable = false)
    private boolean pickByUser = false;
    @Column
    private String achieve;
    @Column(nullable = false)
    private String charName;
    @Column
    private String job;
    @Column
    private int level;

}
