package com.pricecomparator.price_comparator_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record DiscountDto(

        @Schema(example = "P001", description = "Business ID of the discounted product")
        String productId,

        @Schema(example = "lapte zuzu", description = "Name of the discounted product")
        String productName,

        @Schema(example = "Zuzu", description = "Brand of the product")
        String brand,

        @Schema(example = "1.0", description = "Quantity in the package")
        double packageQuantity,

        @Schema(example = "l", description = "Unit of the package")
        String packageUnit,

        @Schema(example = "lactate", description = "Category of the product")
        String productCategory,

        @Schema(example = "2025-05-01", format = "date", description = "Discount start date")
        LocalDate fromDate,

        @Schema(example = "2025-05-07", format = "date", description = "Discount end date")
        LocalDate toDate,

        @Schema(example = "10", description = "Percentage of discount")
        double percentage,

        @Schema(example = "Lidl", description = "Store offering the discount")
        String store,

        @Schema(example = "2025-05-01", format = "date", description = "Date the discount data was imported")
        LocalDate date
) {}
