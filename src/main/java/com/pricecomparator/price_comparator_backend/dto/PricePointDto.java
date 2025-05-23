package com.pricecomparator.price_comparator_backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PricePointDto(
        LocalDate date,
        String store,
        BigDecimal price
) {}
