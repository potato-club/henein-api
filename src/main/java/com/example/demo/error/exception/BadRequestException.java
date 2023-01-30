package com.example.demo.error.exception;

import com.example.demo.error.ErrorCode;

public class BadRequestException extends BusinessException{

    BadRequestException(String message, ErrorCode errorCode) {
        super(errorCode, message);
    }
}
