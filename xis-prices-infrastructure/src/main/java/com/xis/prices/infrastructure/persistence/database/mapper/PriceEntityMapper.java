package com.xis.prices.infrastructure.persistence.database.mapper;

import com.xis.prices.domain.model.Price;
import com.xis.prices.infrastructure.persistence.database.entity.PriceEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PriceEntityMapper {

  Price priceEntityToPrice(PriceEntity priceEntity);
}
