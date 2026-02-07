package com.xis.prices.infrastructure.rest.controller;

import com.xis.prices.api.PricesApi;
import com.xis.prices.model.Price;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
public class PriceApiController implements PricesApi {

  @Override
  public Mono<ResponseEntity<Price>> getPrice(
      @NotNull @Valid final OffsetDateTime applicationDate,
      @NotNull @Valid final Long productId,
      @NotNull @Valid final Integer brandId,
      final ServerWebExchange exchange) {
    return Mono.create((responseEntityMonoSink) -> ResponseEntity.ok());
  }
}
