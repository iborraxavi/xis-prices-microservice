package com.xis.prices.infrastructure.rest.mapper;

import com.xis.prices.application.usecase.getprice.request.GetPriceCommand;
import com.xis.prices.application.usecase.getprice.response.GetPriceResponse;
import com.xis.prices.model.Price;
import java.time.OffsetDateTime;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PriceApiMapper {

  GetPriceCommand getPriceRequestToGetPriceCommand(
      Long productId, Integer brandId, OffsetDateTime applicationDate);

  Price getPriceResponseToPrice(GetPriceResponse getPriceResponse);
}
