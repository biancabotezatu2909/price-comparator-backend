package com.pricecomparator.price_comparator_backend.service;

import com.pricecomparator.price_comparator_backend.dto.*;
import com.pricecomparator.price_comparator_backend.model.Product;
import com.pricecomparator.price_comparator_backend.repository.ProductRepository;
import com.pricecomparator.price_comparator_backend.service.DiscountApplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasketOptimizationService {

    private final ProductRepository productRepository;
    private final DiscountApplierService discountApplierService;
    private final PricePerUnitService pricePerUnitService;

    public OptimizedBasketResponseDto optimizeBasket(ShoppingListRequestDto request) {
        Map<String, List<OptimizedItemDto>> itemsByStore = new HashMap<>();
        List<String> missingProducts = new ArrayList<>();
        BigDecimal bestCaseTotal = BigDecimal.ZERO;

        for (ShoppingItemDto item : request.basket()) {
            List<Product> matches = productRepository.findByProductNameIgnoreCase(item.productName());
            if (matches.isEmpty()) {
                missingProducts.add(item.productName());
                continue;
            }

            List<OptimizedItemDto> options = matches.stream().map(p -> {
                BigDecimal discounted = discountApplierService.getDiscountedPrice(p);
                BigDecimal unitPrice = pricePerUnitService.computePricePerUnit(
                        discounted,
                        Double.parseDouble(String.valueOf(p.getPackageQuantity())),
                        p.getPackageUnit()
                );
                BigDecimal total = discounted.multiply(BigDecimal.valueOf(item.quantity()));
                String unitLabel = pricePerUnitService.getUnitLabel(p.getPackageUnit());

                return new OptimizedItemDto(
                        p.getProductName(),
                        p.getBrand(),
                        p.getStore(),
                        unitPrice,
                        discounted,
                        item.quantity(),
                        total,
                        unitLabel
                );
            }).sorted(Comparator.comparing(OptimizedItemDto::totalPrice)).toList();

            OptimizedItemDto best = options.get(0);
            bestCaseTotal = bestCaseTotal.add(best.totalPrice());
            itemsByStore.computeIfAbsent(best.store(), k -> new ArrayList<>()).add(best);
        }

        // Calculate worst-case total (as before)
        // Only include products that were found
        Set<String> allStores = productRepository.findAll().stream()
                .map(Product::getStore)
                .collect(Collectors.toSet());

        BigDecimal worstCaseTotal = BigDecimal.ZERO;

        for (String store : allStores) {
            boolean validStore = true;
            BigDecimal storeTotal = BigDecimal.ZERO;

            for (ShoppingItemDto item : request.basket()) {
                if (missingProducts.contains(item.productName())) continue;

                List<Product> products = productRepository.findByStoreAndProductNameIgnoreCase(store, item.productName());
                if (products.isEmpty()) {
                    validStore = false;
                    break;
                }

                Product p = products.get(0);
                BigDecimal discounted = discountApplierService.getDiscountedPrice(p);
                storeTotal = storeTotal.add(discounted.multiply(BigDecimal.valueOf(item.quantity())));
            }

            if (validStore) {
                worstCaseTotal = worstCaseTotal.max(storeTotal);
            }
        }

        Map<String, StoreBasketDto> grouped = itemsByStore.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> new StoreBasketDto(
                                e.getKey(),
                                e.getValue().stream().map(OptimizedItemDto::totalPrice).reduce(BigDecimal.ZERO, BigDecimal::add),
                                e.getValue()
                        )
                ));

        return new OptimizedBasketResponseDto(
                bestCaseTotal,
                worstCaseTotal,
                worstCaseTotal.subtract(bestCaseTotal),
                grouped,
                missingProducts
        );
    }



}
