package kr.henein.api.error.exception;

import kr.henein.api.error.ErrorCode;
import lombok.Getter;

@Getter
public class JwtException extends BusinessException{
    public JwtException(String message, ErrorCode errorCode){
        super(message,errorCode);
    }
}
