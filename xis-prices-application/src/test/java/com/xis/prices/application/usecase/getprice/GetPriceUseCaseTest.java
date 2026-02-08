package com.xis.prices.application.usecase.getprice;

import static org.mockito.Mockito.*;

import com.xis.prices.application.usecase.getprice.request.GetPriceCommand;
import com.xis.prices.application.usecase.getprice.response.GetPriceResponse;
import com.xis.prices.domain.model.Price;
import com.xis.prices.domain.repository.PriceRepository;
import java.time.OffsetDateTime;
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
    final Long productId = 1L;
    final Integer brandId = 111;
    final OffsetDateTime applicationDate = OffsetDateTime.now();
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
}
