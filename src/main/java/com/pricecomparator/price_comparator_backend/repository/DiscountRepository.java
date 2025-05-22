package com.pricecomparator.price_comparator_backend.repository;

import com.pricecomparator.price_comparator_backend.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
    List<Discount> findByStore(String store);
    List<Discount> findByToDateGreaterThanEqual(LocalDate date);
    List<Discount> findByFromDateAfter(LocalDate date);
    List<Discount> findTop10ByOrderByPercentageDesc();
}
