package com.xis.prices.domain.model;

import java.time.OffsetDateTime;

public record Price(
    Long productId,
    Integer brandId,
    Long priceList,
    OffsetDateTime startDate,
    OffsetDateTime endDate,
    Double price) {}
