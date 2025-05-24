package com.pricecomparator.price_comparator_backend.model;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "product",
        uniqueConstraints = @UniqueConstraint(columnNames = {"productId", "store", "date"}),
        indexes = @Index(columnList = "productId, store, date"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productId;
    private String productName;
    private String productCategory;
    private String brand;
    private double packageQuantity;
    private String packageUnit;
    private BigDecimal price;
    private String currency;

    private String store;
    private LocalDate date;

}