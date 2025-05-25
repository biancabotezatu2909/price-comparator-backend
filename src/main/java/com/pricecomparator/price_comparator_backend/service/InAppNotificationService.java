package com.pricecomparator.price_comparator_backend.service;

import com.pricecomparator.price_comparator_backend.model.Notification;
import com.pricecomparator.price_comparator_backend.model.User;
import com.pricecomparator.price_comparator_backend.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InAppNotificationService {

    private final NotificationRepository repository;

    public void notify(User user, String title, String message) {
        Notification n = Notification.builder()
                .userEmail(user.getEmail())
                .title(title)
                .message(message)
                .read(false)
                .createdAt(LocalDateTime.now())
                .build();
        repository.save(n);
    }
}
