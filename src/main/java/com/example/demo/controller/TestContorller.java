package com.example.demo.controller;

import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestContorller {
    private final UserService userService;

    @GetMapping("/test")
    public String test(@RequestParam String email,@RequestParam String rt){
        return userService.test(email, rt);
    }
}
