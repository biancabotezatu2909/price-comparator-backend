package com.pricecomparator.price_comparator_backend.service;

import com.pricecomparator.price_comparator_backend.model.Discount;
import com.pricecomparator.price_comparator_backend.model.Product;
import com.pricecomparator.price_comparator_backend.repository.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Service
public class DiscountApplierService {

    @Autowired
    private final DiscountRepository discountRepository;

    public DiscountApplierService(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    public BigDecimal getDiscountedPrice(Product product) {
        return findBestApplicableDiscount(product)
                .map(discount -> product.getPrice().multiply(
                        BigDecimal.valueOf(1 - discount.getPercentage() / 100.0)))
                .orElse(product.getPrice());
    }


    public java.util.Optional<Discount> findBestApplicableDiscount(Product product) {
        List<Discount> discounts = discountRepository.findAll();

        return discounts.stream()
                .filter(d -> d.getProductId().equalsIgnoreCase(product.getProductId()))
                .filter(d -> d.getStore().equalsIgnoreCase(product.getStore()))
                .filter(d -> !product.getDate().isBefore(d.getFromDate()) &&
                        !product.getDate().isAfter(d.getToDate()))
                .max(Comparator.comparingDouble(Discount::getPercentage));
    }
}
