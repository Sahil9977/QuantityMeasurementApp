package com.app.quantitymeasurement.auth.service;

import com.app.quantitymeasurement.auth.dto.AuthResponse;
import com.app.quantitymeasurement.auth.dto.LoginRequest;
import com.app.quantitymeasurement.auth.dto.RegisterRequest;
import com.app.quantitymeasurement.auth.dto.UserProfileResponse;
import com.app.quantitymeasurement.auth.model.AuthProvider;
import com.app.quantitymeasurement.auth.model.User;
//import com.app.quantitymeasurement.auth.model.UserRole;
import com.app.quantitymeasurement.auth.repository.UserRepository;
import com.app.quantitymeasurement.auth.util.JwtUtil;
import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Business logic for user registration, login, and profile retrieval.
 *
 * @Service   → Spring component-scanned, injectable everywhere
 * @Transactional → DB operations are wrapped in a transaction automatically
 */
@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    @Autowired private UserRepository       userRepository;
    @Autowired private PasswordEncoder      passwordEncoder;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtUtil              jwtUtil;

    // ── REGISTER ──────────────────────────────────────────────────────────────

    @Transactional
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new QuantityMeasurementException(
                    "Email already registered: " + request.getEmail());
        }

        // Build and save the new user (Lombok @Builder on User)
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
//                .role(UserRole.ROLE_USER)
                .provider(AuthProvider.LOCAL)
                .emailVerified(false)
                .build();

        userRepository.save(user);
        log.info("Registered new LOCAL user: {}", request.getEmail());

        // Issue JWT immediately so the user is logged in after registration
        String token = jwtUtil.generateTokenFromEmail(user.getEmail());

        return AuthResponse.builder()
                .accessToken(token)
                .expiresIn(jwtUtil.getExpirationMs() / 1000)
                .email(user.getEmail())
                .name(user.getName())
//                .role(user.getRole())
                .provider(user.getProvider())
                .build();
    }

    // ── LOGIN ─────────────────────────────────────────────────────────────────

    public AuthResponse login(LoginRequest request) {

        // Spring Security's AuthenticationManager verifies email + password.
        // Internally it calls UserDetailsServiceImpl.loadUserByUsername()
        // then BCryptPasswordEncoder.matches(rawPassword, storedHash).
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        // Put authentication in the current request's security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails principal =
                (CustomUserDetails) authentication.getPrincipal();
        User user = principal.getUser();

        String token = jwtUtil.generateToken(authentication);
        log.info("Login success for: {}", user.getEmail());

        return AuthResponse.builder()
                .accessToken(token)
                .expiresIn(jwtUtil.getExpirationMs() / 1000)
                .email(user.getEmail())
                .name(user.getName())
//                .role(user.getRole())
                .provider(user.getProvider())
                .build();
    }

    // ── CURRENT USER PROFILE ──────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public UserProfileResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new QuantityMeasurementException(
                        "User not found: " + email));
        return UserProfileResponse.fromUser(user);
    }

    // ── ADMIN: list all users ─────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public java.util.List<UserProfileResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserProfileResponse::fromUser)
                .toList();
    }
}
