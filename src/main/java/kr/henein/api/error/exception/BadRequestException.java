package kr.henein.api.error.exception;

import kr.henein.api.error.ErrorCode;

public class BadRequestException extends BusinessException{

    public BadRequestException(String message, ErrorCode errorCode) {
        super(message,errorCode);
    }
}
