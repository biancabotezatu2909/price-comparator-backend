package com.pricecomparator.price_comparator_backend.service;

import com.pricecomparator.price_comparator_backend.repository.ProductRepository;
import com.pricecomparator.price_comparator_backend.repository.DiscountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataResetService {

    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;

    public void resetAllData() {
        discountRepository.deleteAll(); // delete discounts first to avoid FK issues
        productRepository.deleteAll();
    }
}
