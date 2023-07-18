package com.example.demo.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@ToString
public enum ErrorCode {
  NULL_VALUE(600,HttpStatus.BAD_REQUEST,"데이터가 없습니다."),
  INVALID_USER(400, HttpStatus.BAD_REQUEST, "아이디 또는 비밀번호가 일치하지 않습니다."),
  BAD_REQUEST(400, HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

  DUPLICATE_EMAIL(409, HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
  DUPLICATE_NICKNAME(409, HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
  DUPLICATE_KEY(409, HttpStatus.CONFLICT, "중복된 key 값입니다."),
  FORBIDDEN_EXCEPTION(403, HttpStatus.FORBIDDEN, "해당 요청에 대한 권한이 없습니다."),
  NON_LOGIN(401, HttpStatus.UNAUTHORIZED, "로그인 후 이용 가능합니다"),
  NOT_EXIST(404, HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),
  NOT_FOUND(404, HttpStatus.NOT_FOUND, "페이지를 찾을 수 없습니다."),
  EXPIRED_TOKEN(101, HttpStatus.UNAUTHORIZED, "An expired token. Please try with token refresh"),
  RE_LOGIN(102, HttpStatus.UNAUTHORIZED, "모든 토큰이 만료되었습니다. 다시 로그인해주세요"),
  INVALID_TOKEN(103, HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다. 토큰 재확인"),
  NOT_FOUND_EXCEPTION(404, HttpStatus.BAD_REQUEST, "찾을 수 없는 요청입니다."),

  ALREADY_EXISTS(110,HttpStatus.BAD_REQUEST,"대기시간")
  ;

  private int code;
  private HttpStatus status;
  private String message;
}
