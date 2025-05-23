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

    @Value("${app.import.enabled:false}")
    private boolean importEnabled;
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
        if (!importEnabled) {
            log.info("CSV import skipped — app.import.enabled is false");
            return;
        }

        log.info("CSV import enabled — loading data from folder");
        loadProducts();
        loadDiscounts();
    }

    private void loadProducts() {
        log.info("Loading product CSVs from classpath '{}'", dataFolderPath);
        List<Product> all = productCsvImporter.importFromClasspathFolder("data");
        List<Product> newOnes = all.stream()
                .filter(p -> !productService.exists(p))
                .toList();
        productService.saveAll(newOnes);
        log.info("✅ Saved {} new products ({} skipped as duplicates)", newOnes.size(), all.size() - newOnes.size());
    }

    private void loadDiscounts() {
        List<Discount> all = discountCsvImporter.importFromClasspathFolder("data");
        List<Discount> newOnes = all.stream()
                .filter(d -> !discountService.exists(d))
                .toList();
        discountService.saveAll(newOnes);
        log.info("✅ Saved {} new discounts ({} skipped as duplicates)", newOnes.size(), all.size() - newOnes.size());

    }
}
