package com.example.demo.error;

import com.example.demo.error.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestControllerAdvice
public class ErrorExceptionControllerAdvice {

    @ExceptionHandler({CustomException.class, Exception.class})
    public ResponseEntity<ErrorEntity> handleCustomException(CustomException e) {
        ErrorCode errorCode;
        String message;

        if (e instanceof CustomException) {
            CustomException customException = (CustomException) e;
            errorCode = customException.getErrorCode();
            message = customException.getMessage();
        } else {
            errorCode = ErrorCode.INTERNAL_SERVER_ERROR; // 기존 ErrorCode에 INTERNAL_SERVER_ERROR 항목 추가 필요
            message = "Internal Server Error";
        }

        ErrorEntity errorEntity = new ErrorEntity(errorCode.getCode(), message);
        return new ResponseEntity<>(errorEntity, errorCode.getStatus());
    }

//    @ExceptionHandler({BadRequestException.class})//400
//    public ResponseEntity<ErrorEntity> exceptionHandler(HttpServletRequest request, final BadRequestException e){
//        return ResponseEntity
//                .status(e.getErrorCode().getStatus())
//                .body(ErrorEntity.builder()
//                        .errorCode(e.getErrorCode().getCode())
//                        .errorMessage(e.getErrorCode().getMessage())
//                        .build());
//    }
//    @ExceptionHandler({UnAuthorizedException.class})//401
//    public String handleUnAuthorizedException(UnAuthorizedException e){
//        ErrorEntity errorEntity = new ErrorEntity(e.getErrorCode().getCode() ,e.getMessage());
//        return errorEntity.toString();
//    }
//
//
//    @ExceptionHandler({ForbiddenException.class})//403
//    public ResponseEntity<ErrorEntity> exceptionHandler(HttpServletRequest request, final ForbiddenException e){
//        return ResponseEntity
//                .status(e.getErrorCode().getStatus())
//                .body(ErrorEntity.builder()
//                        .errorCode(e.getErrorCode().getCode())
//                        .errorMessage(e.getErrorCode().getMessage())
//                        .build());
//    }
//
//    @ExceptionHandler({InternerServerException.class})//500
//    public ResponseEntity<ErrorEntity> exceptionHandler(HttpServletRequest request, final InternerServerException e) {
//        return ResponseEntity
//                .status(e.getErrorCode().getStatus())
//                .body(ErrorEntity.builder()
//                        .errorCode(e.getErrorCode().getCode())
//                        .errorMessage(e.getErrorCode().getMessage())
//                        .build());
//    }
//
//    @ExceptionHandler({NotFoundException.class}) //404
//    public ResponseEntity<ErrorEntity> exceptionHandler(HttpServletRequest request, final NotFoundException e){
//        return ResponseEntity
//                .status(e.getErrorCode().getStatus())
//                .body(ErrorEntity.builder()
//                        .errorCode(e.getErrorCode().getCode())
//                        .errorMessage(e.getErrorCode().getMessage())
//                        .build());
//    }

}
