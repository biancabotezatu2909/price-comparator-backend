package com.pricecomparator.price_comparator_backend.repository;

import com.pricecomparator.price_comparator_backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}