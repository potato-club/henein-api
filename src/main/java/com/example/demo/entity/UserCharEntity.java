package com.example.demo.entity;

import com.example.demo.dto.userchar.DetailCharacter;
import com.example.demo.dto.userchar.NodeConnection;
import jdk.jfr.Label;
import lombok.AllArgsConstructor;
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
    private String experience;
    @Column
    @Lob
    private byte[] avatar;
    @Column(nullable = false,unique = true)
    private String nickName;
    @Column
    private String world;
    @Column
    private int level;
    @Column
    private String job;
    @Column
    private String guildId;
    @Column
    private int popularity;


    public UserCharEntity(UserEntity userEntity,String charName) {
        this.userEntity = userEntity;
        this.nickName = charName;
    }
    public void update(DetailCharacter detailCharacter){
       this.experience = detailCharacter.getExperience();
       this.avatar = detailCharacter.getAvatar().getBytes();
       this.world = detailCharacter.getWorld();
       this.level = detailCharacter.getLevel();
       this.job = detailCharacter.getJob();
       this.guildId = detailCharacter.getGuildId();
       this.popularity = detailCharacter.getPopularity();
    }
    public void pickThisCharacter(){
        this.pickByUser = true;
    }
    public void unPickThisCharacter(){
        this.pickByUser = false;
    }
}
