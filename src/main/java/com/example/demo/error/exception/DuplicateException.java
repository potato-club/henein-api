package com.example.demo.error.exception;

import com.example.demo.error.ErrorCode;
import lombok.Getter;

@Getter
public class DuplicateException extends RuntimeException {
  private final ErrorCode errorCode;
  private final String message;

  public DuplicateException(ErrorCode errorCode, String message) {
    this.errorCode = errorCode;
    this.message = message;
  }
}
