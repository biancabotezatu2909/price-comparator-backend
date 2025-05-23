package com.pricecomparator.price_comparator_backend.mapper;

import com.pricecomparator.price_comparator_backend.dto.DiscountDto;
import com.pricecomparator.price_comparator_backend.model.Discount;

public class DiscountMapper {

    public static DiscountDto toDto(Discount d) {
        return new DiscountDto(
                d.getProductId(),
                d.getProductName(),
                d.getBrand(),
                d.getPackageQuantity(),
                d.getPackageUnit(),
                d.getProductCategory(),
                d.getFromDate(),
                d.getToDate(),
                d.getPercentage(),
                d.getStore(),
                d.getDate()
        );
    }
}
