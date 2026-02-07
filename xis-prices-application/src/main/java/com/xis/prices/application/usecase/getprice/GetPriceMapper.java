package com.xis.prices.application.usecase.getprice;

import com.xis.prices.application.usecase.getprice.response.GetPriceResponse;
import com.xis.prices.domain.model.Price;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GetPriceMapper {

  GetPriceResponse priceToGetPriceResponse(Price price);
}
