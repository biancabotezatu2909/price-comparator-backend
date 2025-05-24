package com.pricecomparator.price_comparator_backend.dto;

import java.util.List;

public record ShoppingListRequestDto(List<ShoppingItemDto> basket) {}
