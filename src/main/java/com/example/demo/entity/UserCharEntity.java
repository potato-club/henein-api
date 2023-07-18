package com.example.demo.entity;

import com.example.demo.dto.userchar.Character;
import com.example.demo.dto.userchar.NodeConnection;
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
    private Long experience;
    @Column
    private String avatar;
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
    public void update(Character character){
       this.experience = character.getExperience();
       this.avatar = character.getAvatar();
       this.world = character.getWorld();
       this.level = character.getLevel();
       this.job = character.getJob();
       this.guildId = character.getGuildId();
       this.popularity = character.getPopularity();
    }
}
