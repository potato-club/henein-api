package com.example.demo.error.exception;

import com.example.demo.error.ErrorCode;

public class BadRequestException extends RuntimeException{
    private final ErrorCode errorCode;
    private final String message;

    public BadRequestException(ErrorCode errorCode,String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
