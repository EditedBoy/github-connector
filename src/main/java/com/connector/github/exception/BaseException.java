package com.connector.github.exception;

import com.connector.github.exception.model.ExceptionDetails;
import java.util.Optional;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public abstract class BaseException extends RuntimeException {

  private final ExceptionDetails messageDetails;

  protected BaseException(String message, HttpStatus status) {
    super(message);
    this.messageDetails = new ExceptionDetails(message, status);
  }

  protected BaseException(String message, HttpStatus status, Throwable cause) {
    super(message, cause);
    this.messageDetails = new ExceptionDetails(message, status);
  }

  /**
   * Extracts {@link HttpStatus} from thrown exception instance. Default value is {@link HttpStatus} of {@link BaseException} class.
   *
   * @return {@link HttpStatus} of exception
   */
  public HttpStatus getHttpStatus() {
    return Optional.ofNullable(getClass().getAnnotation(ResponseStatus.class))
        .orElseGet(() -> BaseException.class.getAnnotation(ResponseStatus.class))
        .value();
  }
}
