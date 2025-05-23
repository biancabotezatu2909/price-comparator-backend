package com.pricecomparator.price_comparator_backend.controller;

import com.pricecomparator.price_comparator_backend.dto.DiscountDto;
import com.pricecomparator.price_comparator_backend.mapper.DiscountMapper;
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
    public List<DiscountDto> getAll(){
        return discountService.getAll()
                .stream()
                .map(DiscountMapper::toDto)
                .toList();
    }

    @GetMapping("/best")
    public List<DiscountDto> getBest(
            @RequestParam(required = false) String store,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean activeOnly,
            @RequestParam(required = false) String productName
    ) {
        return discountService.getTopFilteredByPercentage(store, category, activeOnly, productName)
                .stream()
                .map(DiscountMapper::toDto)
                .toList();    }


    @GetMapping("/new")
    public List<DiscountDto> getNewDiscounts(
            @RequestParam(defaultValue = "1") int days,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String store
    ) {
        return discountService.getNewDiscounts(days, category, productName, store)
                .stream()
                .map(DiscountMapper::toDto)
                .toList();
    }


    @GetMapping("/store")
    public List<DiscountDto> getByStore(@RequestParam String store){
        return discountService.getByStore(store)
                .stream()
                .map(DiscountMapper::toDto)
                .toList();
    }

}
