package com.xis.prices.infrastructure.persistence.database.jpa;

import com.xis.prices.infrastructure.persistence.database.entity.PriceEntity;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface JpaPriceRepository extends ReactiveCrudRepository<PriceEntity, UUID> {

  @Query(
      """
    SELECT p.* FROM PRICES p
    WHERE p.BRAND_ID = :brandId
      AND p.PRODUCT_ID = :productId
      AND p.START_DATE <= :applicationDate
      AND p.END_DATE >= :applicationDate
    ORDER BY p.PRIORITY DESC
    """)
  Flux<PriceEntity> findApplicablePrices(
      @Param("productId") Long productId,
      @Param("brandId") Integer brandId,
      @Param("applicationDate") OffsetDateTime applicationDate);
}
