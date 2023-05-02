package com.example.demo.error.exception;

import com.example.demo.error.ErrorCode;
import lombok.Getter;


@Getter
public class LoginFailedException extends RuntimeException {
  private final ErrorCode errorCode;

  public LoginFailedException(ErrorCode errorCode) {
    this.errorCode = errorCode;
  }
}
