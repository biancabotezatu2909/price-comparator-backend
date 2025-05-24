package com.pricecomparator.price_comparator_backend.controller;

import com.pricecomparator.price_comparator_backend.service.DataResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final DataResetService dataResetService;

    @DeleteMapping("/reset-db")
    public String resetDatabase() {
        dataResetService.resetAllData();
        return "All data deleted.";
    }
}
