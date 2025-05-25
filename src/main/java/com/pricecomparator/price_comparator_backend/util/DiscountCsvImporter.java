package com.pricecomparator.price_comparator_backend.util;

import com.pricecomparator.price_comparator_backend.model.Discount;
import com.pricecomparator.price_comparator_backend.model.Product;
import com.pricecomparator.price_comparator_backend.repository.ProductRepository;
import com.pricecomparator.price_comparator_backend.service.DiscountApplierService;
import com.pricecomparator.price_comparator_backend.service.PriceAlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class DiscountCsvImporter {

    private final ProductRepository productRepository;
    private final PriceAlertService priceAlertService;
    private final DiscountApplierService discountApplierService;

    public List<Discount> importFromClasspathFolder(String folderPath) {
        Map<String, Discount> latestDiscountMap = new HashMap<>();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        try {
            Resource[] resources = resolver.getResources("classpath:" + folderPath + "/*_discounts_*.csv");

            for (Resource resource : resources) {
                String filename = resource.getFilename();
                if (filename == null || !filename.endsWith(".csv")) continue;

                String store = extractStoreFromFilename(filename);
                String fileDate = extractDateFromFilename(filename);

                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

                    log.info("Parsing discount file: {}", filename);
                    int lineNumber = 0;
                    String line;
                    boolean headerSkipped = false;

                    while ((line = reader.readLine()) != null) {
                        lineNumber++;

                        if (!headerSkipped) {
                            headerSkipped = true;
                            if (!line.toLowerCase().startsWith("product_id")) {
                                log.warn("Expected header at line 1 in {}, skipping file", filename);
                                break;
                            }
                            continue;
                        }

                        String[] fields = line.trim().split(";");
                        if (fields.length < 9) {
                            log.warn("Malformed line {} in {}: {}", lineNumber, filename, Arrays.toString(fields));
                            continue;
                        }

                        try {
                            Discount discount = Discount.builder()
                                    .productId(fields[0])
                                    .productName(fields[1])
                                    .brand(fields[2])
                                    .packageQuantity(Double.parseDouble(fields[3]))
                                    .packageUnit(fields[4])
                                    .productCategory(fields[5])
                                    .fromDate(LocalDate.parse(fields[6]))
                                    .toDate(LocalDate.parse(fields[7]))
                                    .percentage(Double.parseDouble(fields[8]))
                                    .store(store)
                                    .date(LocalDate.parse(fileDate))
                                    .build();

                            String key = discount.getProductId() + "|" + discount.getStore() + "|" +
                                    discount.getFromDate() + "|" + discount.getToDate();

                            Discount existing = latestDiscountMap.get(key);
                            if (existing == null || discount.getDate().isAfter(existing.getDate())) {
                                latestDiscountMap.put(key, discount);

                                // 🔍 Find all recent products across all stores
                                List<Product> baseProducts = productRepository
                                        .findAllByProductIdOrderByDateDesc(discount.getProductId());

                                for (Product baseProduct : baseProducts) {
                                    Product simulatedProduct = Product.builder()
                                            .productId(baseProduct.getProductId())
                                            .productName(baseProduct.getProductName())
                                            .productCategory(baseProduct.getProductCategory())
                                            .brand(baseProduct.getBrand())
                                            .packageQuantity(baseProduct.getPackageQuantity())
                                            .packageUnit(baseProduct.getPackageUnit())
                                            .store(baseProduct.getStore())
                                            .price(baseProduct.getPrice())
                                            .currency(baseProduct.getCurrency())
                                            .date(discount.getFromDate())
                                            .build();

                                    // ✅ Apply the discount first before checking alerts
                                    Product discountedProduct = discountApplierService.applyDiscount(simulatedProduct, discount);

                                    priceAlertService.checkForTriggeredAlerts(discountedProduct);
                                }
                            }

                        } catch (Exception e) {
                            log.warn("Failed to parse discount at line {} in {}: {}", lineNumber, filename, Arrays.toString(fields), e);
                        }
                    }

                } catch (Exception e) {
                    log.error("Failed to read file {}", filename, e);
                }
            }

        } catch (Exception e) {
            log.error("Failed to load resources from '{}'", folderPath, e);
        }

        List<Discount> deduplicated = new ArrayList<>(latestDiscountMap.values());
        log.info("Returning {} most recent discounts from folder '{}'", deduplicated.size(), folderPath);
        return deduplicated;
    }

    private String extractStoreFromFilename(String filename) {
        try {
            String name = filename.replace(".csv", "");
            return name.split("_discounts_")[0];
        } catch (Exception e) {
            log.warn("⚠️ Could not extract store from filename '{}'", filename);
            return "unknown";
        }
    }

    private String extractDateFromFilename(String filename) {
        try {
            String name = filename.replace(".csv", "");
            return name.split("_discounts_")[1];
        } catch (Exception e) {
            log.warn("⚠️ Could not extract date from filename '{}'", filename);
            return "unknown";
        }
    }
}
