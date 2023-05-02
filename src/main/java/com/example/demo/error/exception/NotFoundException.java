package com.example.demo.error.exception;

import com.example.demo.error.ErrorCode;
import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException{
    private final ErrorCode errorCode;
    private final String message;

    public NotFoundException(ErrorCode errorCode,String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
