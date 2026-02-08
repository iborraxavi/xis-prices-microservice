package com.xis.prices.infrastructure.rest.controller;

import static org.mockito.Mockito.*;

import com.xis.prices.api.model.Price;
import com.xis.prices.application.usecase.getprice.GetPriceUseCase;
import com.xis.prices.application.usecase.getprice.request.GetPriceCommand;
import com.xis.prices.application.usecase.getprice.response.GetPriceResponse;
import com.xis.prices.infrastructure.rest.mapper.PriceApiMapper;
import java.time.OffsetDateTime;
import org.instancio.Instancio;
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
    final Long productId = generateRandomProductId();
    final Integer brandId = generateRandomBrandId();
    final OffsetDateTime applicationDate = generateApplicationDate();
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

  @Test
  @DisplayName("Get price when error then throw expected exception")
  void getPrice_whenError_thenThrowExpectedException() {
    // Given
    final Long productId = generateRandomProductId();
    final Integer brandId = generateRandomBrandId();
    final OffsetDateTime applicationDate = generateApplicationDate();
    final GetPriceCommand getPriceCommand = mock(GetPriceCommand.class);

    when(priceApiMapper.getPriceRequestToGetPriceCommand(productId, brandId, applicationDate))
        .thenReturn(getPriceCommand);
    when(getPriceUseCase.dispatch(getPriceCommand))
        .thenReturn(Mono.error(new NullPointerException()));

    // Act
    StepVerifier.create(priceApiController.getPrice(applicationDate, productId, brandId, null))
        .expectError(NullPointerException.class)
        .verify();

    // Verify
    verify(priceApiMapper, only())
        .getPriceRequestToGetPriceCommand(productId, brandId, applicationDate);
    verify(getPriceUseCase, only()).dispatch(getPriceCommand);
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
