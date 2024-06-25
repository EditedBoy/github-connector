package com.connector.github.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends BaseException {

  public NotFoundException(String message) {
    super(message, HttpStatus.NOT_FOUND);
  }

  public NotFoundException(String message, Throwable cause) {
    super(message, HttpStatus.NOT_FOUND, cause);
  }
}