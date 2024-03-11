package kr.henein.api.dto.userchar;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserMapleApi {
    private String userApi;
    private LocalDate recentDay;
    private LocalDate pastDay;
}
