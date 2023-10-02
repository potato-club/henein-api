package com.example.demo.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data

public class ErrorEntity {
  private int code;
  private String errorMessage;
  @Builder
  public ErrorEntity(HttpStatus httpStatus, int code, String errorMessage) {
    this.code = code;
    this.errorMessage = errorMessage;
  }
}
