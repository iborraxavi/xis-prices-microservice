package com.xis.prices.infrastructure.persistence.database.repository;

import com.xis.prices.domain.model.Price;
import com.xis.prices.domain.repository.PriceRepository;
import com.xis.prices.infrastructure.persistence.database.jpa.JpaPriceRepository;
import com.xis.prices.infrastructure.persistence.database.mapper.PriceEntityMapper;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Component
public class DefaultPriceRepository implements PriceRepository {

  private final JpaPriceRepository jpaPriceRepository;
  private final PriceEntityMapper priceEntityMapper;

  @Override
  public Flux<Price> findApplicablePrices(
      final Long productId, final Integer brandId, final OffsetDateTime applicationDate) {
    return jpaPriceRepository
        .findApplicablePrices(productId, brandId, applicationDate)
        .map(priceEntityMapper::priceEntityToPrice);
  }
}
