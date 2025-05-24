package com.pricecomparator.price_comparator_backend.dto;

import java.math.BigDecimal;

public record BestDiscountedProductDto(
        String productId,
        String productName,
        String brand,
        String store,
        String category,
        BigDecimal originalPrice,
        double discountPercentage,
        BigDecimal discountedPrice
) {}