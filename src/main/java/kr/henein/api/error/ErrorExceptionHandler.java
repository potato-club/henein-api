package kr.henein.api.error;

import kr.henein.api.error.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class ErrorExceptionHandler {
    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<ErrorEntity> exceptionHandler(final BadRequestException e) {
        ErrorEntity errorEntity = ErrorEntity.builder()
                .code(e.getErrorCode().getCode())
                .errorMessage(e.getMessage())
                .build();
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(errorEntity);
    }
    @ExceptionHandler({DuplicateException.class})
    public ResponseEntity<ErrorEntity> exceptionHandler(final DuplicateException e) {
        ErrorEntity errorEntity = ErrorEntity.builder()
                .code(e.getErrorCode().getCode())
                .errorMessage(e.getMessage())
                .build();
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(errorEntity);
    }
    @ExceptionHandler({ForbiddenException.class})
    public ResponseEntity<ErrorEntity> exceptionHandler(final ForbiddenException e) {
        ErrorEntity errorEntity = ErrorEntity.builder()
                .code(e.getErrorCode().getCode())
                .errorMessage(e.getMessage())
                .build();
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(errorEntity);
    }
    @ExceptionHandler({JwtException.class})
    public ResponseEntity<ErrorEntity> exceptionHandler(final JwtException e) {
        ErrorEntity errorEntity = ErrorEntity.builder()
                .code(e.getErrorCode().getCode())
                .errorMessage(e.getMessage())
                .build();
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(errorEntity);
    }
    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ErrorEntity> exceptionHandler(final NotFoundException e) {
        ErrorEntity errorEntity = ErrorEntity.builder()
                .code(e.getErrorCode().getCode())
                .errorMessage(e.getMessage())
                .build();
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(errorEntity);
    }
    @ExceptionHandler({UnAuthorizedException.class})
    public ResponseEntity<ErrorEntity> exceptionHandler(final UnAuthorizedException e) {
        ErrorEntity errorEntity = ErrorEntity.builder()
                .code(e.getErrorCode().getCode())
                .errorMessage(e.getMessage())
                .build();
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(errorEntity);
    }

}
