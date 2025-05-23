package com.pricecomparator.price_comparator_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProductDto(
        @Schema(description = "Business ID of the product", example = "P001")
        String productId,

        @Schema(description = "Name of the product", example = "lapte zuzu")
        String productName,

        @Schema(description = "Product category", example = "lactate")
        String productCategory,

        @Schema(description = "Brand", example = "Zuzu")
        String brand,

        @Schema(description = "Quantity in package", example = "1.0")
        double packageQuantity,

        @Schema(description = "Measurement unit", example = "l")
        String packageUnit,

        @Schema(description = "Product price", example = "9.90")
        BigDecimal price,

        @Schema(description = "Currency", example = "RON")
        String currency,

        @Schema(description = "Store selling the product", example = "Lidl")
        String store,

        @Schema(description = "Date when the price was recorded", example = "2025-05-08", type = "string", format = "date")
        LocalDate date
) {}
