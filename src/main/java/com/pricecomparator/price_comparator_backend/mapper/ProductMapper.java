package com.pricecomparator.price_comparator_backend.mapper;

import com.pricecomparator.price_comparator_backend.dto.ProductDto;
import com.pricecomparator.price_comparator_backend.model.Product;

public class ProductMapper {
    public static ProductDto toDto(Product product) {
        return new ProductDto(
                product.getProductId(),
                product.getProductName(),
                product.getProductCategory(),
                product.getBrand(),
                product.getPackageQuantity(),
                product.getPackageUnit(),
                product.getPrice(),
                product.getCurrency(),
                product.getStore(),
                product.getDate()
        );
    }
}
