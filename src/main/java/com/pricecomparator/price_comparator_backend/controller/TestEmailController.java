package com.pricecomparator.price_comparator_backend.controller;

import com.pricecomparator.price_comparator_backend.model.Product;
import com.pricecomparator.price_comparator_backend.model.User;
import com.pricecomparator.price_comparator_backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
public class TestEmailController {

    private final NotificationService notificationService;

    @GetMapping("/test/email")
    public ResponseEntity<String> testEmail() {
        User mockUser = new User();
        mockUser.setEmail("test@mailtrap.io");

        Product mockProduct = Product.builder()
                .productName("Lapte Zuzu")
                .store("Lidl")
                .price(new BigDecimal("5.99"))
                .build();

        notificationService.sendPriceDropAlert(mockUser, mockProduct);

        return ResponseEntity.ok("✅ Email trigger executed. Check Mailtrap inbox.");
    }
}
