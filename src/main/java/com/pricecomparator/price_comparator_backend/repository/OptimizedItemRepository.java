package com.pricecomparator.price_comparator_backend.repository;

import com.pricecomparator.price_comparator_backend.model.OptimizedItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptimizedItemRepository extends JpaRepository<OptimizedItemEntity, Long> {

    List<OptimizedItemEntity> findByStore(String store);

    List<OptimizedItemEntity> findByProductNameIgnoreCase(String productName);

    List<OptimizedItemEntity> findByStoreAndProductNameIgnoreCase(String store, String productName);

    boolean existsByProductNameAndStoreAndQuantity(
            String productName,
            String store,
            int quantity
    );
}
