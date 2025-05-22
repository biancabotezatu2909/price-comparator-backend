package com.pricecomparator.price_comparator_backend.util;

import com.pricecomparator.price_comparator_backend.model.Discount;
import com.pricecomparator.price_comparator_backend.model.Product;
import com.pricecomparator.price_comparator_backend.service.DiscountService;
import com.pricecomparator.price_comparator_backend.service.ProductService;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class DataLoader {

    private final ProductCsvImporter productCsvImporter;
    private final DiscountCsvImporter discountCsvImporter;
    private final ProductService productService;
    private final DiscountService discountService;

    @Value("${data.folder.path:src/main/resources/data}")
    private String dataFolderPath;

    public DataLoader(ProductCsvImporter productCsvImporter,
                      DiscountCsvImporter discountCsvImporter,
                      ProductService productService,
                      DiscountService discountService) {
        this.productCsvImporter = productCsvImporter;
        this.discountCsvImporter = discountCsvImporter;
        this.productService = productService;
        this.discountService = discountService;
    }

    @PostConstruct
    public void loadInitialData() {
        loadProducts();
        loadDiscounts();
    }

    private void loadProducts() {
        log.info("📦 Loading product CSVs from classpath '{}'", dataFolderPath);
        List<Product> products = productCsvImporter.importFromClasspathFolder("data");
        productService.saveAll(products);
    }

    private void loadDiscounts() {
        log.info("💸 Loading discount CSVs from classpath '{}'", dataFolderPath);
        List<Discount> discounts = discountCsvImporter.importFromClasspathFolder("data");
        discountService.saveAll(discounts);
    }
}
