package com.pricecomparator.price_comparator_backend.service;

import com.pricecomparator.price_comparator_backend.dto.PriceAlertDto;
import com.pricecomparator.price_comparator_backend.model.PriceAlert;
import com.pricecomparator.price_comparator_backend.model.Product;
import com.pricecomparator.price_comparator_backend.model.User;
import com.pricecomparator.price_comparator_backend.repository.PriceAlertRepository;
import com.pricecomparator.price_comparator_backend.repository.ProductRepository;
import com.pricecomparator.price_comparator_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PriceAlertService {

    private final PriceAlertRepository alertRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public void createAlert(String email, PriceAlertDto body) {
        User user = userRepository.findById(email).orElseThrow();

        String productId = body.productId();
        String productName = body.productName();
        String store = body.store();
        BigDecimal targetPrice = body.targetPrice();

        if (productId != null && (productName == null || productName.isBlank())) {
            Optional<Product> product = productRepository.findAllByProductId(productId).stream().findFirst();
            if (product.isEmpty()) {
                throw new IllegalArgumentException("No product found for given productId");
            }
            productName = product.get().getProductName();
        }

        PriceAlert alert = new PriceAlert();
        alert.setProductId(productId);
        alert.setProductName(productName);
        alert.setStore(store);
        alert.setTargetPrice(targetPrice);
        alert.setUser(user);

        alertRepository.save(alert);
    }

    public List<PriceAlertDto> getAlertsForUser(String email) {
        User user = userRepository.findById(email).orElseThrow();
        List<PriceAlert> alerts = alertRepository.findByUser(user);

        return alerts.stream()
                .map(alert -> new PriceAlertDto(
                        alert.getProductId(),
                        alert.getProductName(),
                        alert.getStore(),
                        alert.getTargetPrice()
                ))
                .toList();
    }
}
