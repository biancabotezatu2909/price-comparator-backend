package com.pricecomparator.price_comparator_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PricePerUnitService {

    @Autowired
    private UnitConversionService unitConversionService;

    public BigDecimal computePricePerUnit(BigDecimal price, double quantity, String unit) {
        BigDecimal normalizedQty = unitConversionService.normalizeQuantity(quantity, unit);
        return price.divide(normalizedQty, 2, RoundingMode.HALF_UP);
    }

    public String getUnitLabel(String unit) {
        return unitConversionService.getCanonicalUnit(unit);
    }
}
