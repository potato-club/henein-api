package com.example.demo.dto.userchar;

import com.example.demo.entity.UserCharEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCharacterResponse {
    private Long id;
    private boolean pickByUser;
    private String avatar;
    private String charName;
    private String world;
    private int level;
    private String job;

    public UserCharacterResponse(UserCharEntity userCharEntity){
        this.id = userCharEntity.getId();
        this.pickByUser = userCharEntity.isPickByUser();
        this.avatar = userCharEntity.getAvatar();
        this.charName = userCharEntity.getCharName();
        this.world = userCharEntity.getWorld();
        this.level = userCharEntity.getLevel();
        this.job = userCharEntity.getJob();
    }
}
