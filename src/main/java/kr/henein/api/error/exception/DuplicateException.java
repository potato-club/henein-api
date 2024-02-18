package kr.henein.api.error.exception;

import kr.henein.api.error.ErrorCode;

public class DuplicateException extends BusinessException {

  public DuplicateException(String message, ErrorCode errorCode) {
    super(message, errorCode);
  }
}
