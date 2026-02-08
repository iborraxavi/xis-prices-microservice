package com.xis.prices.application.usecase.getprice;

import com.xis.prices.application.usecase.getprice.request.GetPriceCommand;
import com.xis.prices.application.usecase.getprice.response.GetPriceResponse;
import com.xis.prices.domain.exception.NotFoundException;
import com.xis.prices.domain.repository.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Use case for retrieving price information based on product ID, brand ID, and application date.
 * This class interacts with the {@link PriceRepository} to find applicable prices and uses the
 * {@link GetPriceMapper} to convert domain models to response objects. If no applicable price is
 * found, a {@link NotFoundException} is thrown.
 */
@RequiredArgsConstructor
@Service
public class GetPriceUseCase {

  private final PriceRepository priceRepository;
  private final GetPriceMapper getPriceMapper;

  /**
   * Dispatches the command to retrieve price information. It queries the repository for applicable
   * prices based on the product ID, brand ID, and application date provided in the command. If a
   * price is found, it is mapped to a {@link GetPriceResponse} and returned. If no price is found,
   * a {@link NotFoundException} is thrown with a message indicating the search parameters.
   *
   * @param getPriceCommand the command containing the parameters for retrieving price information
   * @return a Mono emitting the GetPriceResponse if a price is found, or an error if not found
   */
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
