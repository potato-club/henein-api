package com.example.demo.enumCustom;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AchieveType {
    UserAchieve("UA","유저업적"),
    CharacterAchieve("CA","캐릭터업적");
    private final String key;
    private final String title;
}
