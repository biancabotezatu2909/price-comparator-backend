package com.pricecomparator.price_comparator_backend.service;


import com.pricecomparator.price_comparator_backend.model.Discount;
import com.pricecomparator.price_comparator_backend.repository.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiscountService {

    @Autowired
    private DiscountRepository discountRepository;

    public List<Discount> getAll(){
        return discountRepository.findAll();
    }

    public List<Discount> getByStore(String store){
        return discountRepository.findByStore(store);
    }

    public List<Discount> getNewDiscounts(int daysBack, String category, String productName, String store) {
        LocalDate today = LocalDate.now();
        LocalDate threshold = today.minusDays(daysBack);

        return discountRepository.findAll().stream()
                .filter(d -> !d.getFromDate().isBefore(threshold))
                .filter(d -> !d.getToDate().isBefore(today))
                .filter(d -> category == null || d.getProductCategory().equalsIgnoreCase(category))
                .filter(d -> productName == null || normalize(d.getProductName()).contains(normalize(productName)))
                .filter(d -> store == null || d.getStore().equalsIgnoreCase(store))
                .toList();
    }
    

    public List<Discount> getActiveDiscounts(LocalDate onDate){
        return discountRepository.findByFromDateGreaterThanEqual(onDate);
    }

    public List<Discount> getTopByPercentage(int limit){
        return discountRepository.findAll(Sort.by(Sort.Direction.DESC, "percentage"))
                .stream().limit(limit).toList();
    }

    public void saveAll(List<Discount> discounts){
        discountRepository.saveAll(discounts);
    }

    public void deleteAll(){
        discountRepository.deleteAll();
    }

    public List<Discount> getBestDiscounts(String store, String category, String productName) {
        LocalDate today = LocalDate.now();

        return discountRepository.findAll().stream()
                // Filter: only active
                .filter(d -> !d.getFromDate().isAfter(today) && !d.getToDate().isBefore(today))

                // Optional filters
                .filter(d -> store == null || d.getStore().equalsIgnoreCase(store))
                .filter(d -> category == null || d.getProductCategory().equalsIgnoreCase(category))
                .filter(d -> productName == null || normalize(d.getProductName()).contains(normalize(productName)))

                // Group by normalized productName, pick highest percentage
                .collect(Collectors.toMap(
                        d -> normalize(d.getProductName()),
                        d -> d,
                        (d1, d2) -> d1.getPercentage() >= d2.getPercentage() ? d1 : d2
                ))
                .values().stream()
                .sorted(Comparator.comparingDouble(Discount::getPercentage).reversed())
                .toList();
    }


    public boolean exists(Discount d) {
        return discountRepository.existsByProductIdAndStoreAndFromDateAndToDate(
                d.getProductId(),
                d.getStore(),
                d.getFromDate(),
                d.getToDate()
        );
    }


    private String normalize(String input) {
        return input == null ? "" : input.trim().toLowerCase();
    }

}
