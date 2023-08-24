package com.example.demo.error.exception;

import com.example.demo.error.ErrorCode;
import lombok.Getter;

public class DuplicateException extends BusinessException {

  public DuplicateException(String message, ErrorCode errorCode) {
    super(message, errorCode);
  }
}
