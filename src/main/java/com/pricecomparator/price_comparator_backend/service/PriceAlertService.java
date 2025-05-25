package com.pricecomparator.price_comparator_backend.service;

import com.pricecomparator.price_comparator_backend.dto.PriceAlertDto;
import com.pricecomparator.price_comparator_backend.model.PriceAlert;
import com.pricecomparator.price_comparator_backend.model.Product;
import com.pricecomparator.price_comparator_backend.model.User;
import com.pricecomparator.price_comparator_backend.repository.PriceAlertRepository;
import com.pricecomparator.price_comparator_backend.repository.ProductRepository;
import com.pricecomparator.price_comparator_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
@Slf4j
public class PriceAlertService {

    private final PriceAlertRepository alertRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final DiscountApplierService discountApplierService;
    private final InAppNotificationService inAppNotificationService;

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

    public void checkForTriggeredAlerts(Product product) {
        List<PriceAlert> alerts = alertRepository.findByProductIdAndTriggeredFalse(product.getProductId());
        log.info("Found {} active alerts for product {}", alerts.size(), product.getProductId());

        for (PriceAlert alert : alerts) {
            BigDecimal finalPrice = discountApplierService.getDiscountedPrice(product);

            log.info("Comparing: final price {} vs. alert target {} for user {}",
                    finalPrice, alert.getTargetPrice(), alert.getUser().getEmail());

            if (finalPrice.compareTo(alert.getTargetPrice()) <= 0) {
                alert.setTriggered(true);
                alert.setTriggeredAt(LocalDateTime.now());
                alertRepository.save(alert);

                inAppNotificationService.notify(
                        alert.getUser(),
                        "Price Alert Triggered",
                        String.format("'%s' dropped to %.2f RON at %s.",
                                product.getProductName(), finalPrice, product.getStore())
                );
            }

        }
    }


}
