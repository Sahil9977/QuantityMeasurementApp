package com.app.quantitymeasurement.auth.dto;

import com.app.quantitymeasurement.auth.model.AuthProvider;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Returned by login, register, and OAuth2 token exchange.
 *
 * Lombok @Builder allows: AuthResponse.builder().accessToken(...).build()
 * Swagger @Schema documents each field in Swagger UI "Try it out" responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "JWT authentication response")
public class AuthResponse {

    @Schema(description = "JWT Bearer token — paste into 'Authorize' in Swagger UI",
            example = "eyJhbGciOiJIUzI1NiJ9...")
    private String accessToken;

    @Schema(description = "Token type", example = "Bearer")
    @Builder.Default
    private String tokenType = "Bearer";

    @Schema(description = "Token validity in seconds", example = "86400")
    private long expiresIn;

    @Schema(description = "Authenticated user's email")
    private String email;

    @Schema(description = "Authenticated user's display name")
    private String name;
//
//    @Schema(description = "User role: ROLE_USER or ROLE_ADMIN")
//    private UserRole role;

    @Schema(description = "How the user authenticated: LOCAL or GOOGLE")
    private AuthProvider provider;
}
