package com.pricecomparator.price_comparator_backend.dto;

import java.math.BigDecimal;

public record PriceAlertDto(
        String productId,
        String productName,
        String store,
        BigDecimal targetPrice
) {}
