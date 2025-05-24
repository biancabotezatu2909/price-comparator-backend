package com.pricecomparator.price_comparator_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String email;

    private String password;

   // @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  //  private List<PriceAlert> alerts = new ArrayList<>();
}
