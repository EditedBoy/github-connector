package com.connector.github.handler;

import com.connector.github.exception.BaseException;
import com.connector.github.exception.model.ExceptionDetails;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BaseException.class)
  public Mono<ResponseEntity<?>> handleBaseException(BaseException ex) {
    log.error("Handle BaseException", ex);
    return Mono.just(ResponseEntity
        .status(ex.getHttpStatus())
        .contentType(MediaType.APPLICATION_JSON)
        .body(ex.getMessageDetails()));
  }

  @ExceptionHandler(Exception.class)
  public Mono<ResponseEntity<?>> handleDefaultException(Exception ex) {
    log.error("Handle Exception", ex);
    return Mono.just(ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .contentType(MediaType.APPLICATION_JSON)
        .body(new ExceptionDetails(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR)));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Mono<ResponseEntity<?>> handleValidationExceptions(MethodArgumentNotValidException ex) {
    log.error("Handle MethodArgumentNotValidException", ex);
    var errorDetails = ex.getBindingResult().getFieldErrors()
        .stream()
        .findAny()
        .map(error -> new ExceptionDetails(error.getDefaultMessage(), HttpStatus.BAD_REQUEST))
        .orElse(new ExceptionDetails("Validation exception", HttpStatus.BAD_REQUEST));

    return Mono.just(ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .contentType(MediaType.APPLICATION_JSON)
        .body(errorDetails));
  }

  @ExceptionHandler(WebClientResponseException.class)
  public Mono<ResponseEntity<?>> handleWebClientResponseException(WebClientResponseException ex) {
    log.error("Handle WebClientResponseException", ex);
    return Mono.just(ResponseEntity
        .status(ex.getStatusCode())
        .contentType(MediaType.APPLICATION_JSON)
        .body(new ExceptionDetails(ex.getStatusCode().value(), ex.getResponseBodyAsString())));
  }

  @ExceptionHandler(ResponseStatusException.class)
  public Mono<ResponseEntity<ExceptionDetails>> handleBadRequestException(ResponseStatusException ex) {
    log.error("Handle ResponseStatusException", ex);
    return Mono.just(ResponseEntity
        .status(ex.getStatusCode())
        .contentType(MediaType.APPLICATION_JSON)
        .body(new ExceptionDetails(ex.getStatusCode().value(), ex.getReason())));
  }
}
