package com.pricecomparator.price_comparator_backend;

import com.pricecomparator.price_comparator_backend.model.Product;
import com.pricecomparator.price_comparator_backend.service.ProductService;
import com.pricecomparator.price_comparator_backend.util.ProductCsvImporter;
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
}

