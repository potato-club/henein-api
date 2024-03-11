package kr.henein.api.dto.userchar;

import lombok.Getter;

@Getter
public class CharacterBasic {
    private String ocid;
    private String character_name;
    private String world_name;
    private String character_class;
    private int character_level;
    private String character_image;
}
