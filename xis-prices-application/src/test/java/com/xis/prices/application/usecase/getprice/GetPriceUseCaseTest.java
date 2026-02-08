package com.xis.prices.application.usecase.getprice;

import static org.mockito.Mockito.*;

import com.xis.prices.application.usecase.getprice.request.GetPriceCommand;
import com.xis.prices.application.usecase.getprice.response.GetPriceResponse;
import com.xis.prices.domain.exception.NotFoundException;
import com.xis.prices.domain.model.Price;
import com.xis.prices.domain.repository.PriceRepository;
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
class GetPriceUseCaseTest {

  @Mock private PriceRepository priceRepository;
  @Mock private GetPriceMapper getPriceMapper;
  @InjectMocks private GetPriceUseCase getPriceUseCase;

  @Test
  @DisplayName("Dispatch when success should return expected response")
  void dispatch_whenSuccess_shouldReturnExpectedResponse() {
    // Given
    final Long productId = generateRandomProductId();
    final Integer brandId = generateRandomBrandId();
    final OffsetDateTime applicationDate = generateApplicationDate();
    final GetPriceCommand getPriceCommand = mock(GetPriceCommand.class);
    final Price price = mock(Price.class);
    final GetPriceResponse getPriceResponse = mock(GetPriceResponse.class);

    when(getPriceCommand.productId()).thenReturn(productId);
    when(getPriceCommand.brandId()).thenReturn(brandId);
    when(getPriceCommand.applicationDate()).thenReturn(applicationDate);
    when(priceRepository.findApplicablePrices(productId, brandId, applicationDate))
        .thenReturn(Flux.just(price));
    when(getPriceMapper.priceToGetPriceResponse(price)).thenReturn(getPriceResponse);

    // Act
    StepVerifier.create(getPriceUseCase.dispatch(getPriceCommand))
        .expectNext(getPriceResponse)
        .expectComplete()
        .verify();

    // Verify
    verify(priceRepository, only()).findApplicablePrices(productId, brandId, applicationDate);
    verify(getPriceMapper, only()).priceToGetPriceResponse(price);
  }

  @Test
  @DisplayName("Dispatch when empty prices list should throw expected exception")
  void dispatch_whenEmptyPricesList_shouldThrowExpectedException() {
    // Given
    final Long productId = generateRandomProductId();
    final Integer brandId = generateRandomBrandId();
    final OffsetDateTime applicationDate = generateApplicationDate();
    final GetPriceCommand getPriceCommand = mock(GetPriceCommand.class);

    when(getPriceCommand.productId()).thenReturn(productId);
    when(getPriceCommand.brandId()).thenReturn(brandId);
    when(getPriceCommand.applicationDate()).thenReturn(applicationDate);
    when(priceRepository.findApplicablePrices(productId, brandId, applicationDate))
        .thenReturn(Flux.empty());

    // Act
    StepVerifier.create(getPriceUseCase.dispatch(getPriceCommand))
        .expectError(NotFoundException.class)
        .verify();

    // Verify
    verify(priceRepository, only()).findApplicablePrices(productId, brandId, applicationDate);
    verifyNoInteractions(getPriceMapper);
  }

  @Test
  @DisplayName("Dispatch when find applicable prices error should throw expected exception")
  void dispatch_whenFindApplicablePricesError_shouldThrowExpectedException() {
    // Given
    final Long productId = generateRandomProductId();
    final Integer brandId = generateRandomBrandId();
    final OffsetDateTime applicationDate = generateApplicationDate();
    final GetPriceCommand getPriceCommand = mock(GetPriceCommand.class);

    when(getPriceCommand.productId()).thenReturn(productId);
    when(getPriceCommand.brandId()).thenReturn(brandId);
    when(getPriceCommand.applicationDate()).thenReturn(applicationDate);
    when(priceRepository.findApplicablePrices(productId, brandId, applicationDate))
        .thenReturn(Flux.error(new IllegalArgumentException()));

    // Act
    StepVerifier.create(getPriceUseCase.dispatch(getPriceCommand))
        .expectError(IllegalArgumentException.class)
        .verify();

    // Verify
    verify(priceRepository, only()).findApplicablePrices(productId, brandId, applicationDate);
    verifyNoInteractions(getPriceMapper);
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
