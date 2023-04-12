package com.example.demo.error;

//import com.example.demo.error.exception.UnAuthorizedException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
//@AllArgsConstructor
public enum ErrorCode {
    RUNTIME_EXCEPTION(HttpStatus.BAD_REQUEST,001,"잘못된 요청방식입니다."),
//    ACCESS_DENIED_EXCEPTION(HttpStatus.UNAUTHORIZED,"E0001","로그인하세요."),
//    DUPLICATE_EMAIL(HttpStatus.UNAUTHORIZED,"E0021","가입되어 있는 이메일 입니다."),
//    FORBIDDEN_EXCEPTION(HttpStatus.FORBIDDEN,"E0003","인가되지 않은 사용자 입니다."),
//    UNDEFINED_TIME(HttpStatus.FORBIDDEN,"E00032","정의되어있는 시간이 없습니다."),
//    NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND,"E0004","잘못된 주소입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 105, "Internal Server Error"),
    DO_NOT_RESOLVE_AT(HttpStatus.UNAUTHORIZED,100,"헤더에 AT 미존재"),
    EXPIRED_TOKEN( HttpStatus.UNAUTHORIZED,101, "유효기간 지난 토큰"),
    INVALID_TOKEN( HttpStatus.UNAUTHORIZED,102, "잘못된 정보의 토큰입니다."),
    UNSUPPORTED_TOKEN( HttpStatus.UNAUTHORIZED,103, "이상한 토큰. 백에게 연락바람"),
    NON_LOGIN(HttpStatus.UNAUTHORIZED,104, "Not logged in user");
    private HttpStatus status;

    private int code;

    private String message;

    ErrorCode(HttpStatus status, int code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
