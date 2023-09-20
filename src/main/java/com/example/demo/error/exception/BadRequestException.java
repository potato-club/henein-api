package com.example.demo.error.exception;

import com.example.demo.error.ErrorCode;

public class BadRequestException extends BusinessException{

    public BadRequestException(String message, ErrorCode errorCode) {
        super(message,errorCode);
    }
}
