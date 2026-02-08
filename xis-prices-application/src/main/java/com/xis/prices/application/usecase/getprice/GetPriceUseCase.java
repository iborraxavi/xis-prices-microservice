package com.xis.prices.application.usecase.getprice;

import com.xis.prices.application.usecase.getprice.request.GetPriceCommand;
import com.xis.prices.application.usecase.getprice.response.GetPriceResponse;
import com.xis.prices.domain.exception.NotFoundException;
import com.xis.prices.domain.repository.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class GetPriceUseCase {

  private final PriceRepository priceRepository;
  private final GetPriceMapper getPriceMapper;

  public Mono<GetPriceResponse> dispatch(final GetPriceCommand getPriceCommand) {
    return priceRepository
        .findApplicablePrices(
            getPriceCommand.productId(),
            getPriceCommand.brandId(),
            getPriceCommand.applicationDate())
        .next()
        .map(getPriceMapper::priceToGetPriceResponse)
        .switchIfEmpty(
            Mono.error(
                new NotFoundException(
                    "No applicable price found for productId: %s, brandId: %s and applicationDate: %s"
                        .formatted(
                            getPriceCommand.productId(),
                            getPriceCommand.brandId(),
                            getPriceCommand.applicationDate()))));
  }
}
