package kr.henein.api.enumCustom;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum S3EntityType {

    USER("ROLE_USER", "유저 엔티티"),
    BOARD("ROLE_BOARD", "게시글 엔티티"),
    NON_USED("ROLE_NON","정해지지 않음");

    private final String key;
    private final String title;
}
