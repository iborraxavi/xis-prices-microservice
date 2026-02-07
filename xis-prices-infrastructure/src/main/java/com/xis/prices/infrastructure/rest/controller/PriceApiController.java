package com.xis.prices.infrastructure.rest.controller;

import com.xis.prices.api.PricesApi;
import com.xis.prices.application.usecase.getprice.GetPriceUseCase;
import com.xis.prices.infrastructure.rest.mapper.PriceApiMapper;
import com.xis.prices.model.Price;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PriceApiController implements PricesApi {

  private final GetPriceUseCase getPriceUseCase;
  private final PriceApiMapper priceApiMapper;

  @Override
  public Mono<ResponseEntity<Price>> getPrice(
      @NotNull @Valid final OffsetDateTime applicationDate,
      @NotNull @Valid final Long productId,
      @NotNull @Valid final Integer brandId,
      final ServerWebExchange exchange) {
    return Mono.just(
            priceApiMapper.getPriceRequestToGetPriceCommand(productId, brandId, applicationDate))
        .doOnNext(
            getPriceCommand ->
                log.debug(
                    "Get price request received with productId: {}, brandId: {}, applicationDate: {}",
                    getPriceCommand.productId(),
                    getPriceCommand.brandId(),
                    getPriceCommand.applicationDate()))
        .flatMap(getPriceUseCase::dispatch)
        .map(priceApiMapper::getPriceResponseToPrice)
        .map(ResponseEntity::ok);
  }
}
