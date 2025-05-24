package com.pricecomparator.price_comparator_backend.controller;

import com.pricecomparator.price_comparator_backend.dto.PricePointDto;
import com.pricecomparator.price_comparator_backend.dto.ProductDto;
import com.pricecomparator.price_comparator_backend.dto.ValuePerUnitDto;
import com.pricecomparator.price_comparator_backend.mapper.ProductMapper;
import com.pricecomparator.price_comparator_backend.model.Product;
import com.pricecomparator.price_comparator_backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<ProductDto> getFilteredProducts(
            @RequestParam(required = false) String store,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String productId
    ) {
        List<Product> products;

        if (store != null && category != null) {
            products = productService.getByStoreAndCategory(store, category);
        } else if (store != null && productId != null) {
            products = productService.getByStoreAndProductId(store, productId);
        } else if (store != null) {
            products = productService.getByStore(store);
        } else if (category != null) {
            products = productService.getByCategory(category);
        } else if (productId != null) {
            products = productService.getByProductId(productId);
        } else {
            products = productService.getAll();
        }
        return products.stream().map(ProductMapper::toDto).toList();
    }

    @GetMapping("/products/{productId}/price-history")
    public List<PricePointDto> getPriceHistory(
            @PathVariable String productId,
            @RequestParam(required = false) String store,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String category,
            @RequestParam(required = false)
            @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE)
            LocalDate from,
            @RequestParam(required = false)
            @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE)
            LocalDate to
    ) {
        return productService.getPriceHistory(productId, store, brand, category, from, to);
    }

    @GetMapping("/products/recommendations")
    public List<ValuePerUnitDto> getRecommendedProducts(
            @RequestParam String productName,
            @RequestParam(required = false) String brand
    ) {
        return productService.getRecommendedProducts(productName, brand);
    }



}
