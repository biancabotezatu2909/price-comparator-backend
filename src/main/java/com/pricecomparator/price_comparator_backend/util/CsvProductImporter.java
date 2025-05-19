package com.pricecomparator.price_comparator_backend.util;

import com.pricecomparator.price_comparator_backend.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
@Slf4j
public class CsvProductImporter {

    public List<Product> importFromClasspathFolder(String folderPath) {
        List<Product> allProducts = new ArrayList<>();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        try {
            Resource[] resources = resolver.getResources("classpath:" + folderPath + "/*.csv");

            for (Resource resource : resources) {
                String filename = resource.getFilename();
                if (filename == null || !filename.endsWith(".csv")) continue;

                String[] parts = filename.replace(".csv", "").split("_");
                if (parts.length != 2) continue; // skip malformed filenames

                String store = parts[0];
                String date = parts[1];

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                        resource.getInputStream(), StandardCharsets.UTF_8))) {

                    log.info("Parsing file: {}", filename);
                    int lineNumber = 1;
                    String line;
                    while ((line = reader.readLine()) != null) {
                        lineNumber++;
                        String[] fields = line.split(";");
                       // log.info("Line {} from {}: {}", lineNumber, filename, Arrays.toString(fields));

                        if (fields.length < 8) {
                          //  log.warn("Skipped line {} in {}: invalid field count ({})", lineNumber, filename, fields.length);
                            continue;
                        }

                        if (fields[0].equalsIgnoreCase("product_id")) {
                            log.info("Skipped header row at line {} in {}", lineNumber, filename);
                            continue;
                        }
                        Product product = Product.builder()
                                .productId(fields[0])
                                .productName(fields[1])
                                .productCategory(fields[2])
                                .brand(fields[3])
                                .packageQuantity(Double.parseDouble(fields[4]))
                                .packageUnit(fields[5])
                                .price(Double.parseDouble(fields[6]))
                                .currency(fields[7])
                                .store(store)
                                .date(date)
                                .build();

                        allProducts.add(product);
                    }

                } catch (Exception e) {
                    log.error("Failed to parse file {}", filename, e);
                }
            }

        } catch (Exception e) {
            log.error("Failed to read resources from {}", folderPath, e);
        }

        return allProducts;
    }
}
