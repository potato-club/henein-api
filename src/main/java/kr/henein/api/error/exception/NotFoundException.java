package kr.henein.api.error.exception;

import kr.henein.api.error.ErrorCode;
import lombok.Getter;

@Getter
public class NotFoundException extends BusinessException{

    public NotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
