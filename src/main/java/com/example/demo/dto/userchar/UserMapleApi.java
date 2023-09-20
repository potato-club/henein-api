package com.example.demo.dto.userchar;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class UserMapleApi {
    private String userApi;
    private LocalDate recentDay;
    private LocalDate pastDay;
}
