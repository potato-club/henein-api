package com.example.demo.dto.userchar;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class Character {
    String id;
    Long experience;
    String avatar;
    String world;
    String nickname;
    int level;
    String job;
    String guildId;
    int popularity;

}
