package com.pricecomparator.price_comparator_backend.dto;

import java.math.BigDecimal;
import java.util.List;

public record StoreBasketDto(
        String store,
        BigDecimal storeTotal,
        List<OptimizedItemDto> items
) {}