package com.example.demo.error.exception;


import com.example.demo.error.ErrorCode;
import lombok.Getter;


@Getter
public class AuthenticationException extends RuntimeException {
  private final ErrorCode errorCode;

  public AuthenticationException(ErrorCode errorCode) {
    this.errorCode = errorCode;
  }
}
