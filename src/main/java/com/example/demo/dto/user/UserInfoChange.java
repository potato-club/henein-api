package com.example.demo.dto.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UserInfoChange {
    private MultipartFile image;
    private String userName;

}
