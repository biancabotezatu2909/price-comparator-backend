package com.pricecomparator.price_comparator_backend.controller;

import com.pricecomparator.price_comparator_backend.model.Notification;
import com.pricecomparator.price_comparator_backend.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationsController {

    private final NotificationRepository notificationRepository;

    @GetMapping
    public List<Notification> getMyNotifications() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return notificationRepository.findByUserEmailAndReadFalseOrderByCreatedAtDesc(email);
    }

    @PostMapping("/{id}/read")
    public void markAsRead(@PathVariable Long id) {
        notificationRepository.findById(id).ifPresent(n -> {
            n.setRead(true);
            notificationRepository.save(n);
        });
    }
}
