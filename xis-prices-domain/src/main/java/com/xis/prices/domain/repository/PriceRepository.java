package com.xis.prices.domain.repository;

import com.xis.prices.domain.model.Price;
import java.time.OffsetDateTime;
import reactor.core.publisher.Flux;

/**
 * Repository interface for managing price data. It provides methods to retrieve prices based on
 * specific criteria such as product ID, brand ID, and application date.
 */
public interface PriceRepository {

  /**
   * Finds applicable prices for a given product ID, brand ID, and application date. The method
   * returns a Flux of Price objects that match the specified criteria.
   *
   * @param productId the ID of the product for which to find prices
   * @param brandId the ID of the brand for which to find prices
   * @param applicationDate the date and time for which to find applicable prices
   * @return a Flux emitting Price objects that match the specified criteria
   */
  Flux<Price> findApplicablePrices(Long productId, Integer brandId, OffsetDateTime applicationDate);
}
