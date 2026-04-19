package com.app.quantitymeasurement.auth.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Represents a registered user.
 * Supports both LOCAL (email/password) and GOOGLE OAuth2 accounts.
 *
 * Lombok annotations used:
 *   @Data            → generates getters, setters, toString, equals, hashCode
 *   @NoArgsConstructor / @AllArgsConstructor → generates constructors
 *   @Builder         → fluent builder pattern for easy object creation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_user_email",    columnList = "email",    unique = true),
        @Index(name = "idx_user_provider", columnList = "provider")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    /** Bcrypt-hashed password — null for Google OAuth2 users */
    @Column(name = "password_hash")
    private String passwordHash;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private UserRole role;

    /** LOCAL or GOOGLE */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider provider;

    /** Google subject ID — set only for GOOGLE users */
    @Column(name = "provider_id")
    private String providerId;

    /** Profile picture URL from Google */
    @Column(name = "image_url", length = 1024)
    private String imageUrl;

    @Column(name = "email_verified")
    @Builder.Default
    private boolean emailVerified = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
//        if (role == null)      role     = UserRole.ROLE_USER;
        if (provider == null)  provider = AuthProvider.LOCAL;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
