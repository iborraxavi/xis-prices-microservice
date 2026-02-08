package com.xis.prices.infrastructure.rest.controller;

import com.xis.prices.api.PricesApi;
import com.xis.prices.api.model.Price;
import com.xis.prices.application.usecase.getprice.GetPriceUseCase;
import com.xis.prices.infrastructure.rest.mapper.PriceApiMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * REST controller for handling price-related API requests. Implements the {@link PricesApi}
 * interface and uses the {@link GetPriceUseCase} to retrieve price information based on the
 * provided parameters. The controller also utilizes the {@link PriceApiMapper} to convert between
 * API request/response objects and domain models.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class PriceApiController implements PricesApi {

  private final GetPriceUseCase getPriceUseCase;
  private final PriceApiMapper priceApiMapper;

  /**
   * Retrieves the price for a given product, brand, and application date.
   *
   * @param applicationDate the date and time for which to retrieve the price
   * @param productId the ID of the product for which to retrieve the price
   * @param brandId the ID of the brand for which to retrieve the price
   * @param exchange the server web exchange (not used in this method)
   * @return a Mono emitting a ResponseEntity containing the Price if found, or an error if not
   *     found
   */
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
