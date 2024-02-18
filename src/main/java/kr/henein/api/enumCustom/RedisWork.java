package kr.henein.api.enumCustom;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RedisWork {
    WORK(1,"work"),
    DONE(2,"done"),
    FAIL(3,"fail");

    private final int key;
    private final String title;
}
