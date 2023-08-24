package com.example.demo.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data

public class ErrorEntity {
  private int errorCode;
  private String errorMessage;
  @Builder
  public ErrorEntity(HttpStatus httpStatus, int errorCode, String errorMessage) {
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
  }
}
