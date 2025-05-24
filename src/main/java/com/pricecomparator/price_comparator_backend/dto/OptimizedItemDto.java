package com.pricecomparator.price_comparator_backend.dto;

import java.math.BigDecimal;

public record OptimizedItemDto(
        String productName,
        String brand,
        String store,
        BigDecimal unitPrice,
        BigDecimal discountedPrice,
        int quantity,
        BigDecimal totalPrice,
        String unitLabel) {}
