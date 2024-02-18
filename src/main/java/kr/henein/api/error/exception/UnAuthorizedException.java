package kr.henein.api.error.exception;

import kr.henein.api.error.ErrorCode;
import lombok.Getter;


@Getter
public class UnAuthorizedException extends BusinessException {

  public UnAuthorizedException(String message, ErrorCode errorCode) {
    super(message,errorCode);
  }
}
