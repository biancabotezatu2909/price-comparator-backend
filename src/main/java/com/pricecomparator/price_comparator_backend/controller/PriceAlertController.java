package com.pricecomparator.price_comparator_backend.controller;

import com.pricecomparator.price_comparator_backend.dto.PriceAlertDto;
import com.pricecomparator.price_comparator_backend.service.PriceAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alerts")
@RequiredArgsConstructor
public class PriceAlertController {

    private final PriceAlertService alertService;

    @PostMapping
    public ResponseEntity<?> createAlert(@RequestBody PriceAlertDto body) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            alertService.createAlert(email, body);
            return ResponseEntity.status(201).body("Alert created");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<PriceAlertDto>> getMyAlerts() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(alertService.getAlertsForUser(email));
    }
}
