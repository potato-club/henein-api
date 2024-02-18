package kr.henein.api.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorJwtCode {
    EXPIRED_AT(101, "access token has expired. Please try with token refresh"),
    INVALID_TOKEN(103, "Invalid JWT token."),
    EMPTY_TOKEN(104, "Token cannot has been null"),
    JWT_COMPLEX_ERROR(4006, "JWT Complex error, Please call BackEnd");

    private final int code;
    private final String message;
}
