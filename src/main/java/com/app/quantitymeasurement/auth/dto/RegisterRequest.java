package com.app.quantitymeasurement.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request body for POST /api/v1/auth/register.
 *
 * Lombok @Data generates: getters, setters, toString, equals, hashCode.
 * Swagger @Schema documents each field in Swagger UI.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Registration request payload")
public class RegisterRequest {

    @Schema(description = "Full name of the user", example = "Rahul Sharma")
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be 2–100 characters")
    private String name;

    @Schema(description = "Email address (used as login)", example = "rahul@example.com")
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid address")
    private String email;

    @Schema(description = "Password (min 6 chars)", example = "secret123")
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be 6–100 characters")
    private String password;


    
    
}


