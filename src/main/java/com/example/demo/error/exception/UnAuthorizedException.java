package com.example.demo.error.exception;

import com.example.demo.error.ErrorCode;
import lombok.Getter;


@Getter
public class UnAuthorizedException extends BusinessException {

  public UnAuthorizedException(String message, ErrorCode errorCode) {
    super(message,errorCode);
  }
}
