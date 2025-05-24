package com.pricecomparator.price_comparator_backend.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record OptimizedBasketResponseDto(
        BigDecimal totalBestCase,
        BigDecimal totalWorstCase,
        BigDecimal savings,
        Map<String, StoreBasketDto> groupedByStore,
        List<String> missingProducts
) {}
