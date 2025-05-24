package com.pricecomparator.price_comparator_backend.service;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

   /* @Value("${JWT_SECRET_KEY}")
    private String secret;*/

    private final SecretKey secureKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);


    public String generateToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(Duration.ofHours(4))))
                .signWith(secureKey)
                .compact();
    }

    public String extractUserId(String token) {
        return Jwts.parser()
                .setSigningKey(secureKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            extractUserId(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
