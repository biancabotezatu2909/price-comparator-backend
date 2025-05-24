package com.pricecomparator.price_comparator_backend.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class UnitConversionService {

    private static final Map<String, ConversionRule> rules = Map.of(
            "g", new ConversionRule("kg", 0.001),
            "kg", new ConversionRule("kg", 1.0),
            "ml", new ConversionRule("l", 0.001),
            "l", new ConversionRule("l", 1.0),
            "buc", new ConversionRule("buc", 1.0),
            "role", new ConversionRule("buc", 1.0)
    );

    public BigDecimal normalizeQuantity(double quantity, String unit) {
        ConversionRule rule = rules.get(unit.toLowerCase());
        if (rule == null) {
            throw new IllegalArgumentException("Unknown unit: " + unit);
        }
        return BigDecimal.valueOf(quantity * rule.multiplier());
    }

    public String getCanonicalUnit(String unit) {
        ConversionRule rule = rules.get(unit.toLowerCase());
        return rule != null ? rule.baseUnit() : unit;
    }

    private record ConversionRule(String baseUnit, double multiplier) {}
}
