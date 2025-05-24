package com.pricecomparator.price_comparator_backend.controller;

import com.pricecomparator.price_comparator_backend.dto.AuthRequest;
import com.pricecomparator.price_comparator_backend.model.User;
import com.pricecomparator.price_comparator_backend.repository.UserRepository;
import com.pricecomparator.price_comparator_backend.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody AuthRequest request) {
        if (userRepository.existsByUsername(request.email())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        User user = new User();
        user.setEmail(request.email());
        user.setPassword(request.password()); // 🔒 Add hashing later
        userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("token", token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        Optional<User> userOpt = userRepository.findById(request.email());

        if (userOpt.isPresent() && userOpt.get().getPassword().equals(request.password())) {
            String token = jwtService.generateToken(userOpt.get().getEmail());
            return ResponseEntity.ok(Map.of("token", token));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
}
