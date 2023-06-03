package com.example.demo.error.exception;


import com.example.demo.error.ErrorCode;
import lombok.Getter;


@Getter
public class AuthenticationException extends RuntimeException {
  private final ErrorCode errorCode;
  private final String message;

  public AuthenticationException(ErrorCode errorCode) {
    this.errorCode = errorCode;
    this.message =null;
  }
  public AuthenticationException(ErrorCode errorCode,String message) {
    this.errorCode = errorCode;
    this.message = message;
  }
}
