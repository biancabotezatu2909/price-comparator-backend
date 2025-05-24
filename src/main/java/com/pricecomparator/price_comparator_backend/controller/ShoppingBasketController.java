package com.pricecomparator.price_comparator_backend.controller;

import com.pricecomparator.price_comparator_backend.dto.OptimizedBasketResponseDto;
import com.pricecomparator.price_comparator_backend.dto.ShoppingListRequestDto;
import com.pricecomparator.price_comparator_backend.service.BasketOptimizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shopping-basket")
@RequiredArgsConstructor
public class ShoppingBasketController {

    private final BasketOptimizationService basketOptimizationService;

    @PostMapping("/optimize")
    public ResponseEntity<OptimizedBasketResponseDto> optimizeBasket(
            @RequestBody ShoppingListRequestDto request
    ) {
        return ResponseEntity.ok(basketOptimizationService.optimizeBasket(request));
    }
}
