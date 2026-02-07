package com.xis.prices.domain.repository;

import com.xis.prices.domain.model.Price;
import java.time.OffsetDateTime;
import reactor.core.publisher.Mono;

public interface PriceRepository {

  Mono<Price> getPrice(Long productId, Integer brandId, OffsetDateTime applicationDate);
}
