package com.pricecomparator.price_comparator_backend.service;

import com.pricecomparator.price_comparator_backend.model.Product;
import com.pricecomparator.price_comparator_backend.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;

    public void sendPriceDropAlert(User user, Product product) {
        String to = user.getEmail();
        String subject = "📉 Price Alert: " + product.getProductName();
        String text = String.format("""
        Hey %s,

        Good news! The price for '%s' at %s just dropped to %.2f RON.
        Check it out in store or in your app.

        — Price Comparator
        """, user.getEmail(), product.getProductName(), product.getStore(), product.getPrice());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        try {
            mailSender.send(message);
            System.out.println("✅ Email sent to " + to);
        } catch (Exception e) {
            System.err.println("❌ Email sending failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
