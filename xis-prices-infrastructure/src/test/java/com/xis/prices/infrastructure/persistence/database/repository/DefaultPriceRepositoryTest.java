package com.xis.prices.infrastructure.persistence.database.repository;

import static org.mockito.Mockito.*;

import com.xis.prices.domain.model.Price;
import com.xis.prices.infrastructure.persistence.database.entity.PriceEntity;
import com.xis.prices.infrastructure.persistence.database.jpa.JpaPriceRepository;
import com.xis.prices.infrastructure.persistence.database.mapper.PriceEntityMapper;
import java.time.OffsetDateTime;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class DefaultPriceRepositoryTest {

  @Mock private JpaPriceRepository jpaPriceRepository;
  @Mock private PriceEntityMapper priceEntityMapper;
  @InjectMocks private DefaultPriceRepository defaultPriceRepository;

  @Test
  @DisplayName("Find applicable prices when success should return expected flux")
  void findApplicablePrices_whenSuccess_shouldReturnExpectedFlux() {
    // Given
    final Long productId = generateRandomProductId();
    final Integer brandId = generateRandomBrandId();
    final OffsetDateTime applicationDate = generateApplicationDate();
    final PriceEntity firstPriceEntity = mock(PriceEntity.class);
    final PriceEntity secondPriceEntity = mock(PriceEntity.class);
    final Price firstPrice = mock(Price.class);
    final Price secondPrice = mock(Price.class);

    when(jpaPriceRepository.findApplicablePrices(productId, brandId, applicationDate))
        .thenReturn(Flux.just(firstPriceEntity, secondPriceEntity));
    when(priceEntityMapper.priceEntityToPrice(firstPriceEntity)).thenReturn(firstPrice);
    when(priceEntityMapper.priceEntityToPrice(secondPriceEntity)).thenReturn(secondPrice);

    // Act
    StepVerifier.create(
            defaultPriceRepository.findApplicablePrices(productId, brandId, applicationDate))
        .expectNext(firstPrice)
        .expectNext(secondPrice)
        .verifyComplete();

    // Verify
    verify(jpaPriceRepository, only()).findApplicablePrices(productId, brandId, applicationDate);
    verify(priceEntityMapper, times(1)).priceEntityToPrice(firstPriceEntity);
    verify(priceEntityMapper, times(1)).priceEntityToPrice(secondPriceEntity);
    verifyNoMoreInteractions(priceEntityMapper);
  }

  @Test
  @DisplayName("Find applicable prices when prices not found should return expected flux")
  void findApplicablePrices_whenPricesNotFound_shouldReturnExpectedFlux() {
    // Given
    final Long productId = generateRandomProductId();
    final Integer brandId = generateRandomBrandId();
    final OffsetDateTime applicationDate = generateApplicationDate();

    when(jpaPriceRepository.findApplicablePrices(productId, brandId, applicationDate))
        .thenReturn(Flux.empty());

    // Act
    StepVerifier.create(
            defaultPriceRepository.findApplicablePrices(productId, brandId, applicationDate))
        .expectNextCount(0)
        .verifyComplete();

    // Verify
    verify(jpaPriceRepository, only()).findApplicablePrices(productId, brandId, applicationDate);
    verifyNoInteractions(priceEntityMapper);
  }

  @Test
  @DisplayName("Find applicable prices when error should throw expected exception")
  void findApplicablePrices_whenError_shouldThrowExpectedException() {
    // Given
    final Long productId = generateRandomProductId();
    final Integer brandId = generateRandomBrandId();
    final OffsetDateTime applicationDate = generateApplicationDate();

    when(jpaPriceRepository.findApplicablePrices(productId, brandId, applicationDate))
        .thenReturn(Flux.error(new IllegalStateException("Database error")));

    // Act
    StepVerifier.create(
            defaultPriceRepository.findApplicablePrices(productId, brandId, applicationDate))
        .expectError(IllegalStateException.class)
        .verify();

    // Verify
    verify(jpaPriceRepository, only()).findApplicablePrices(productId, brandId, applicationDate);
    verifyNoInteractions(priceEntityMapper);
  }

  private Long generateRandomProductId() {
    return Instancio.gen().longs().range(Long.MIN_VALUE, Long.MAX_VALUE).get();
  }

  private Integer generateRandomBrandId() {
    return Instancio.gen().ints().range(Integer.MIN_VALUE, Integer.MAX_VALUE).get();
  }

  private OffsetDateTime generateApplicationDate() {
    return Instancio.gen()
        .temporal()
        .offsetDateTime()
        .range(OffsetDateTime.now().minusYears(10), OffsetDateTime.now())
        .get();
  }
}
