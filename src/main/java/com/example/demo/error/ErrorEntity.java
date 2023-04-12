package com.example.demo.error;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
public class ErrorEntity {
    private int errorCode;

    private String errorMessage;

    @Builder
    public ErrorEntity(int errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
