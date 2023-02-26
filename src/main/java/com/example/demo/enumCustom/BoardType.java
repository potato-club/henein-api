package com.example.demo.enumCustom;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BoardType {
    Advertise("TypeAd","광고게시판"),
    Boss("TypeBo","보스게시판"),
    Free("TypeFr","자유게시판"),
    Humor("TypeHu","유머게시판"),
    Info("TypeIn","정보게시판"),
    Notice("TypeNo","공지게시판");

    private final String key;
    private final String title;
}
