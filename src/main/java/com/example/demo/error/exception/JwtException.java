package com.example.demo.error.exception;

import lombok.Getter;

@Getter
public class JwtException extends RuntimeException{
    private final int errorCode;
    public JwtException(String message, int errorCode){
        super(message);
        this.errorCode = errorCode;
    }
}
