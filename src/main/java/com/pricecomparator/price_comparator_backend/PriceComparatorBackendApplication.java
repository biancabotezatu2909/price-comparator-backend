package com.pricecomparator.price_comparator_backend;

import com.pricecomparator.price_comparator_backend.model.Product;
import com.pricecomparator.price_comparator_backend.service.ProductService;
import com.pricecomparator.price_comparator_backend.util.CsvProductImporter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class PriceComparatorBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PriceComparatorBackendApplication.class, args);
	}

	@Bean
	CommandLineRunner loadCsv(ProductService productService, CsvProductImporter csvProductImporter) {
		return args -> {
			List<Product> products = csvProductImporter.importFromClasspathFolder("data");
			productService.deleteAll();
			productService.saveAll(products);
			System.out.println("Imported: " + products.size() + " products from csv");
		};
	}
}
