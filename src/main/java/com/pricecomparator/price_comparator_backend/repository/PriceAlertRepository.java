package com.pricecomparator.price_comparator_backend.repository;

import com.pricecomparator.price_comparator_backend.model.PriceAlert;
import com.pricecomparator.price_comparator_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceAlertRepository extends JpaRepository<PriceAlert, Long> {
    List<PriceAlert> findByUserEmail(String email);
    List<PriceAlert> findByUserEmailAndTriggeredFalse(String email);
    List<PriceAlert> findByUser(User user);

}
