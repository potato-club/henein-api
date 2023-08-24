package com.example.demo.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorJwtCode {
    EXPIRED_AT(101, "access token has expired. Please try with token refresh"),
    EXPIRED_RT(102, "refresh token has expired. Please try with token refresh"),
    INVALID_TOKEN(103, "Invalid JWT token."),
    EMPTY_TOKEN(104, "Token cannot has been null"),
    UNSUPPORTED_TOKEN(105,"Token has not supported"),
    SIGNATURE_MISMATCH(106, "JWT signature does not match, Please try in accurate token"),
    JWT_COMPLEX_ERROR(4006, "JWT Complex error, Please call BackEnd");

    private final int code;
    private final String message;
}
