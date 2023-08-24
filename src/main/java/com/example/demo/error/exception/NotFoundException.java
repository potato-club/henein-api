package com.example.demo.error.exception;

import com.example.demo.error.ErrorCode;
import lombok.Getter;

@Getter
public class NotFoundException extends BusinessException{

    public NotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
