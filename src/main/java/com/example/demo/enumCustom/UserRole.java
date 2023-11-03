package com.example.demo.enumCustom;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    USER(0, "일반 사용자"),
    WRITER(1,"작성자"),
    ADMIN(2, "관리자");

    private final int key;
    private final String title;
}
