package com.example.demo.dto.userchar;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ApiServerRequestDto {
    private List<String> nameList = new ArrayList<>();
    private List<String> ocidList = new ArrayList<>();
}
