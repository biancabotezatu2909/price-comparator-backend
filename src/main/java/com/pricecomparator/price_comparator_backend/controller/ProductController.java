package com.pricecomparator.price_comparator_backend.controller;

import com.pricecomparator.price_comparator_backend.model.Product;
import com.pricecomparator.price_comparator_backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getFilteredProducts(
            @RequestParam(required = false) String store,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String productId
    ) {


        if (store != null && category != null) {
            return productService.getByStoreAndCategory(store, category);
        }
        if (store != null && productId != null) {
            return productService.getByStoreAndProductId(store, productId);
        }
        if (store != null) {
            return productService.getByStore(store);
        }
        if (category != null) {
            return productService.getByCategory(category);
        }
        if (productId != null) {
            return productService.getByProductId(productId);
        }
        return productService.getAll();
    }
}
