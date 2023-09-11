package com.example.demo.dto.userchar;

import com.example.demo.entity.UserCharEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
public class UserCharacter {
    private Long id;
    private boolean pickByUser;
    private byte[] avatar;
    private String nickName;
    private String world;
    private int level;
    private String job;

    public UserCharacter(UserCharEntity userCharEntity){
        this.id = userCharEntity.getId();
        this.pickByUser = userCharEntity.isPickByUser();
        this.avatar = userCharEntity.getAvatar();
        this.nickName = userCharEntity.getNickName();
        this.world = userCharEntity.getWorld();
        this.level = userCharEntity.getLevel();
        this.job = userCharEntity.getJob();
    }
}
