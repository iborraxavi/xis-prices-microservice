package com.xis.prices.infrastructure.rest.exception;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.xis.prices.domain.exception.NotFoundException;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

  @Mock private ServerWebExchange exchange;
  @Mock private ServerHttpRequest request;
  @InjectMocks private GlobalExceptionHandler globalExceptionHandler;

  private static final String TEST_MESSAGE = "Test error message";
  private static final String TEST_PATH = "/api/v1/test";

  @BeforeEach
  void setUp() {
    when(exchange.getRequest()).thenReturn(request);
    when(request.getPath()).thenReturn(RequestPath.parse(TEST_PATH, null));
  }

  @Test
  @DisplayName("Handle NotFoundException should return NOT_FOUND status with correct error details")
  void handleNotFoundException_whenNotFoundException_shouldReturnExpectedError() {
    // Given
    final NotFoundException exception = new NotFoundException(TEST_MESSAGE);
    final OffsetDateTime beforeCall = OffsetDateTime.now();

    // Act
    final var result = globalExceptionHandler.handleNotFoundException(exception, exchange);
    final OffsetDateTime afterCall = OffsetDateTime.now();

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    assertNotNull(result.getBody());
    assertEquals(TEST_MESSAGE, result.getBody().getMessage());
    assertEquals(TEST_PATH, result.getBody().getPath());
    assertTrue(result.getBody().getDate().isAfter(beforeCall.minusSeconds(1)));
    assertTrue(result.getBody().getDate().isBefore(afterCall.plusSeconds(1)));
  }

  @Test
  @DisplayName(
      "Handle RuntimeException without ResponseStatus annotation should return INTERNAL_SERVER_ERROR status")
  void handleRuntimeException_whenNotResponseStatusAnnotation_shouldReturnInternalServerError() {
    // Given
    final RuntimeException exception = new RuntimeException(TEST_MESSAGE);
    final OffsetDateTime beforeCall = OffsetDateTime.now();

    // Act
    final var result = globalExceptionHandler.handleRuntimeException(exception, exchange);
    final OffsetDateTime afterCall = OffsetDateTime.now();

    // Assert
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    assertNotNull(result.getBody());
    assertEquals(TEST_MESSAGE, result.getBody().getMessage());
    assertEquals(TEST_PATH, result.getBody().getPath());
    assertTrue(result.getBody().getDate().isAfter(beforeCall.minusSeconds(1)));
    assertTrue(result.getBody().getDate().isBefore(afterCall.plusSeconds(1)));
  }

  @Test
  @DisplayName(
      "Handle RuntimeException with ResponseStatus annotation should return status from annotation")
  void handleRuntimeException_whenResponseStatusAnnotation_shouldReturnExpectedError() {
    // Given
    final CustomBadRequestException exception = new CustomBadRequestException(TEST_MESSAGE);
    final OffsetDateTime beforeCall = OffsetDateTime.now();

    // Act
    final var result = globalExceptionHandler.handleRuntimeException(exception, exchange);
    final OffsetDateTime afterCall = OffsetDateTime.now();

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    assertNotNull(result.getBody());
    assertEquals(TEST_MESSAGE, result.getBody().getMessage());
    assertEquals(TEST_PATH, result.getBody().getPath());
    assertTrue(result.getBody().getDate().isAfter(beforeCall.minusSeconds(1)));
    assertTrue(result.getBody().getDate().isBefore(afterCall.plusSeconds(1)));
  }

  @Test
  @DisplayName(
      "Handle RuntimeException when ResponseStatusException should return status from exception")
  void handleRuntimeException_whenResponseStatusException_shouldReturnExpectedError() {
    // Given
    final ResponseStatusException exception =
        new ResponseStatusException(HttpStatus.CONFLICT, TEST_MESSAGE);
    final OffsetDateTime beforeCall = OffsetDateTime.now();

    // Act
    final var result = globalExceptionHandler.handleRuntimeException(exception, exchange);
    final OffsetDateTime afterCall = OffsetDateTime.now();

    // Assert
    assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
    assertNotNull(result.getBody());
    assertTrue(result.getBody().getMessage().contains(TEST_MESSAGE));
    assertEquals(TEST_PATH, result.getBody().getPath());
    assertTrue(result.getBody().getDate().isAfter(beforeCall.minusSeconds(1)));
    assertTrue(result.getBody().getDate().isBefore(afterCall.plusSeconds(1)));
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  private static class CustomBadRequestException extends RuntimeException {

    public CustomBadRequestException(String message) {
      super(message);
    }
  }
}
