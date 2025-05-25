package com.pricecomparator.price_comparator_backend.controller;

import com.pricecomparator.price_comparator_backend.model.Discount;
import com.pricecomparator.price_comparator_backend.model.Product;
import com.pricecomparator.price_comparator_backend.service.DiscountService;
import com.pricecomparator.price_comparator_backend.service.ProductService;
import com.pricecomparator.price_comparator_backend.util.DiscountCsvImporter;
import com.pricecomparator.price_comparator_backend.util.ProductCsvImporter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/import")
@RequiredArgsConstructor
@Slf4j
public class ImportController {

    private final ProductCsvImporter productCsvImporter;
    private final DiscountCsvImporter discountCsvImporter;
    private final ProductService productService;
    private final DiscountService discountService;

    @PostMapping("/all")
    public ResponseEntity<String> importAll() {
        log.info("🔁 Triggered manual /import/all");

        List<Product> products = productCsvImporter.importFromClasspathFolder("data");
        List<Product> newProducts = products.stream()
                .filter(p -> !productService.exists(p))
                .toList();
        productService.saveAll(newProducts);
        log.info("✅ Saved {} new products", newProducts.size());

        List<Discount> discounts = discountCsvImporter.importFromClasspathFolder("data");
        List<Discount> newDiscounts = discounts.stream()
                .filter(d -> !discountService.exists(d))
                .toList();
        discountService.saveAll(newDiscounts);
        log.info("✅ Saved {} new discounts", newDiscounts.size());

        return ResponseEntity.ok("Import completed. Products: " + newProducts.size() + ", Discounts: " + newDiscounts.size());
    }
}
