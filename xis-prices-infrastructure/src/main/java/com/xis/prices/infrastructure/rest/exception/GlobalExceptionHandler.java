package com.xis.prices.infrastructure.rest.exception;

import com.xis.prices.api.model.Error;
import com.xis.prices.domain.exception.NotFoundException;
import java.time.OffsetDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<Error> handleNotFoundException(
      final NotFoundException ex, final ServerWebExchange exchange) {
    log.error("Not found error: {}", ex.getMessage());

    return new ResponseEntity<>(buildErrorResponse(ex, exchange), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Error> handleRuntimeException(
      final RuntimeException ex, final ServerWebExchange exchange) {
    log.error("Unexpected error: {}", ex.getMessage(), ex);

    final HttpStatus status = extractHttpStatus(ex);

    return new ResponseEntity<>(buildErrorResponse(ex, exchange), status);
  }

  private Error buildErrorResponse(final Exception exception, final ServerWebExchange exchange) {
    return new Error()
        .message(exception.getMessage())
        .path(exchange.getRequest().getPath().value())
        .date(OffsetDateTime.now());
  }

  private HttpStatus extractHttpStatus(final RuntimeException ex) {
    if (ex instanceof ResponseStatusException responseStatusEx) {
      return HttpStatus.valueOf(responseStatusEx.getStatusCode().value());
    }

    final ResponseStatus responseStatus = ex.getClass().getAnnotation(ResponseStatus.class);
    if (responseStatus != null) {
      return responseStatus.value();
    }

    return HttpStatus.INTERNAL_SERVER_ERROR;
  }
}
