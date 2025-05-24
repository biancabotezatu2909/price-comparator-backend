package com.pricecomparator.price_comparator_backend.dto;

import java.math.BigDecimal;

public record ValuePerUnitDto(
        String productId,
        String productName,
        String brand,
        String store,
        String category,
        BigDecimal price,
        double packageQuantity,
        String packageUnit,
        BigDecimal pricePerUnit,
        String baseUnit
) {}
