package com.example.demo.error.Exception;

import com.example.demo.error.ErrorCode;
import lombok.Getter;

@Getter
public class NotValidException extends IllegalStateException {
  private final ErrorCode errorCode;

  public NotValidException(ErrorCode errorCode) {
    this.errorCode = errorCode;
  }
}
