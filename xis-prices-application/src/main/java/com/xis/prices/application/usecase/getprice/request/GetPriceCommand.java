package com.xis.prices.application.usecase.getprice.request;

import java.time.OffsetDateTime;

public record GetPriceCommand(OffsetDateTime applicationDate, Long productId, Integer brandId) {}
