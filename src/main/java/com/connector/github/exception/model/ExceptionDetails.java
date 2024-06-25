package com.connector.github.exception.model;

import org.springframework.http.HttpStatus;

public record ExceptionDetails(
    Integer status,
    String message
) {

  public ExceptionDetails(String message, HttpStatus status) {
    this(status.value(), message);
  }

  public ExceptionDetails(String message, Integer status) {
    this(status, message);
  }
}
