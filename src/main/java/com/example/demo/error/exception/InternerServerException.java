package com.example.demo.error.exception;

import com.example.demo.error.ErrorCode;

public class InternerServerException extends BusinessException{
    public InternerServerException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
