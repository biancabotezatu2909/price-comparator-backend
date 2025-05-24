package com.pricecomparator.price_comparator_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "optimized_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OptimizedItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;
    private String brand;
    private String store;
    private BigDecimal unitPrice;
    private BigDecimal discountedPrice;
    private int quantity;
    private BigDecimal totalPrice;
}
