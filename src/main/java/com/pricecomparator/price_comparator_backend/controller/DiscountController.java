package com.pricecomparator.price_comparator_backend.controller;

import com.pricecomparator.price_comparator_backend.model.Discount;
import com.pricecomparator.price_comparator_backend.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/discounts")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @GetMapping
    public List<Discount> getAll(){
        return discountService.getAll();
    }

    @GetMapping("/best")
    public List<Discount> getBest(
            @RequestParam(required = false) String store,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean activeOnly,
            @RequestParam(required = false) String productName
    ) {
        return discountService.getTopFilteredByPercentage(store, category, activeOnly, productName);
    }


    @GetMapping("/new")
    public List<Discount> getNew(@RequestParam("since") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate since){
        return discountService.getNewDiscounts(since);
    }

    @GetMapping("/store")
    public List<Discount> getByStore(@RequestParam String store){
        return discountService.getByStore(store);
    }

}
