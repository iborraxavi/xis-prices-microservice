package com.xis.prices.application.usecase.getprice.response;

import java.time.OffsetDateTime;

public record GetPriceResponse(
    Long productId,
    Integer brandId,
    Long priceList,
    OffsetDateTime startDate,
    OffsetDateTime endDate,
    Double price) {}
