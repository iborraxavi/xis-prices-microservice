package com.xis.prices.infrastructure.rest.controller;

import static org.mockito.Mockito.*;

import com.xis.prices.application.usecase.getprice.GetPriceUseCase;
import com.xis.prices.application.usecase.getprice.request.GetPriceCommand;
import com.xis.prices.application.usecase.getprice.response.GetPriceResponse;
import com.xis.prices.infrastructure.rest.mapper.PriceApiMapper;
import com.xis.prices.model.Price;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class PriceApiControllerTest {

  @Mock private GetPriceUseCase getPriceUseCase;
  @Mock private PriceApiMapper priceApiMapper;
  @InjectMocks private PriceApiController priceApiController;

  @Test
  @DisplayName("Get price when success then return price")
  void getPrice_whenSuccess_thenReturnPrice() {
    // Given
    final Long productId = 1L;
    final Integer brandId = 1;
    final OffsetDateTime applicationDate = OffsetDateTime.now();
    final GetPriceCommand getPriceCommand = mock(GetPriceCommand.class);
    final GetPriceResponse getPriceResponse = mock(GetPriceResponse.class);
    final Price price = mock(Price.class);

    when(priceApiMapper.getPriceRequestToGetPriceCommand(productId, brandId, applicationDate))
        .thenReturn(getPriceCommand);
    when(getPriceUseCase.dispatch(getPriceCommand)).thenReturn(Mono.just(getPriceResponse));
    when(priceApiMapper.getPriceResponseToPrice(getPriceResponse)).thenReturn(price);

    // Act
    StepVerifier.create(priceApiController.getPrice(applicationDate, productId, brandId, null))
        .expectNextMatches(
            priceResponseEntity ->
                priceResponseEntity.getBody() != null
                    && priceResponseEntity.getBody().equals(price))
        .verifyComplete();

    // Verify
    verify(priceApiMapper, times(1))
        .getPriceRequestToGetPriceCommand(productId, brandId, applicationDate);
    verify(priceApiMapper, times(1)).getPriceResponseToPrice(getPriceResponse);
    verifyNoMoreInteractions(priceApiMapper);
    verify(getPriceUseCase, only()).dispatch(getPriceCommand);
  }
}
