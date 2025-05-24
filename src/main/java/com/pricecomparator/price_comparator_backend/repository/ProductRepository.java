package com.pricecomparator.price_comparator_backend.repository;

import com.pricecomparator.price_comparator_backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByStore(String store);

    List<Product> findByProductCategory(String productCategory);

    List<Product> findByStoreAndProductCategory(String store, String productCategory);

    List<Product> findAllByProductId(String productId);

    List<Product> findByStoreAndProductId(String store, String productId);

    boolean existsByProductIdAndStoreAndDate(
            String productId,
            String store,
            LocalDate date
    );
}