package com.pricecomparator.price_comparator_backend.service;

import com.pricecomparator.price_comparator_backend.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailtrapApiService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${MAILTRAP_API_TOKEN}")
    private String apiToken;

    @Value("${MAILTRAP_INBOX_FROM}")
    private String from;

    public void sendPriceDropEmail(User user, String subject, String body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> payload = Map.of(
                "from", Map.of("email", from),
                "to", new Map[]{Map.of("email", user.getEmail())},
                "subject", subject,
                "text", body
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://send.api.mailtrap.io/api/send",
                    request,
                    String.class
            );
            System.out.println("📬 Mailtrap API response: " + response.getStatusCode());
        } catch (Exception e) {
            System.err.println("❌ Mailtrap API send failed: " + e.getMessage());
        }
    }
}
