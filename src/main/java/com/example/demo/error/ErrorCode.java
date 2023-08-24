package com.example.demo.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@ToString
public enum ErrorCode {
  NULL_VALUE(HttpStatus.BAD_REQUEST,403,"데이터가 없습니다."),
  BAD_REQUEST(HttpStatus.BAD_REQUEST,400, "잘못된 요청입니다."),

  INVALID_ACCESS(HttpStatus.UNAUTHORIZED, 401,"인증 오류가 발생했습니다."),
  DUPLICATE_EMAIL(HttpStatus.UNAUTHORIZED,401,"이미 존재하는 이메일입니다."),
  DUPLICATE_NICKNAME(HttpStatus.UNAUTHORIZED,401,"이미 존재하는 닉네임입니다."),
  NON_LOGIN(HttpStatus.UNAUTHORIZED,401, "로그인 후 이용 가능합니다"),
  MISMATCH_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED,401,"잘못된 RT입니다."),
  FORBIDDEN_EXCEPTION(HttpStatus.FORBIDDEN, 403, "해당 요청에 대한 권한이 없습니다."),
  DUPLICATE_KEY(HttpStatus.FORBIDDEN, 403,  "중복된 key 값입니다."),
  NOT_EXIST(HttpStatus.NOT_FOUND,404, "존재하지 않는 유저입니다."),
  NOT_FOUND(HttpStatus.NOT_FOUND,404, "페이지를 찾을 수 없습니다."),
  NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND,404, "찾을 수 없는 요청입니다."),

  ALREADY_EXISTS(HttpStatus.FORBIDDEN, 110,"대기시간")
  ;

  private int code;
  private String message;
  private HttpStatus httpStatus;
  private
  ErrorCode(HttpStatus status, int code, String message) {
    this.httpStatus = status;
    this.code = code;
    this.message = message;
  }
}
