package com.app.quantitymeasurement.auth.controller;

import com.app.quantitymeasurement.auth.dto.AuthResponse;
import com.app.quantitymeasurement.auth.dto.LoginRequest;
import com.app.quantitymeasurement.auth.dto.RegisterRequest;
import com.app.quantitymeasurement.auth.dto.UserProfileResponse;
import com.app.quantitymeasurement.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Authentication & User Management REST controller.
 *
 * Public endpoints (no JWT needed):
 *   POST /api/v1/auth/register  → register a new local user
 *   POST /api/v1/auth/login     → login, receive JWT
 *   GET  /api/v1/auth/google    → redirect to Google OAuth2 consent screen
 *
 * Protected endpoints (JWT required in "Authorization: Bearer <token>" header):
 *   GET  /api/v1/auth/me        → current user's profile
 *
 * Admin-only endpoints (JWT + ROLE_ADMIN required):
 *   GET  /api/v1/auth/admin/users  → list all registered users
 *
 * Swagger Annotations:
 *   @Tag          → groups these endpoints under "Authentication" in Swagger UI
 *   @Operation    → describes each endpoint in Swagger UI
 *   @ApiResponses → documents possible HTTP responses in Swagger UI
 *   @SecurityRequirement("bearerAuth") → shows 🔒 lock icon in Swagger UI;
 *                  user must click "Authorize" and paste their JWT
 */
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Register, Login (JWT), Google OAuth2, and User Management")
public class AuthController {

    @Autowired
    private AuthService authService;

    // ── POST /register ────────────────────────────────────────────────────────

    @Operation(
        summary     = "Register a new local user",
        description = "Creates a new account with email + password. Returns a JWT token."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "User created, JWT returned"),
        @ApiResponse(responseCode = "400", description = "Validation failed or email taken")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ── POST /login ───────────────────────────────────────────────────────────

    @Operation(
        summary     = "Login with email and password",
        description = "Authenticates credentials and returns a Bearer JWT. " +
                      "Copy the accessToken value and paste it into the 'Authorize' " +
                      "button at the top of Swagger UI to test protected endpoints."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login successful, JWT returned"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    // ── GET /google ───────────────────────────────────────────────────────────

    @Operation(
        summary     = "Initiate Google OAuth2 login",
        description = "Redirects the browser to Google's consent screen. " +
                      "After approval, Google redirects back and a JWT is issued. " +
                      "The JWT is returned to the frontend via the redirect URI " +
                      "configured in app.oauth2.redirect-uri."
    )
    @GetMapping("/google")
    public ResponseEntity<Map<String, String>> googleLoginInfo() {
        // The actual OAuth2 redirect is handled by Spring Security at:
        //   GET /oauth2/authorization/google
        // This endpoint just provides the URL for documentation / frontend use.
        return ResponseEntity.ok(Map.of(
            "googleLoginUrl", "/oauth2/authorization/google",
            "description",   "Redirect browser to googleLoginUrl to start Google login"
        ));
    }

    // ── GET /me ───────────────────────────────────────────────────────────────

    @Operation(
        summary     = "Get current user's profile",
        description = "Returns the profile of the authenticated user. " +
                      "Requires a valid JWT in the Authorization header."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profile returned"),
        @ApiResponse(responseCode = "401", description = "JWT missing or invalid")
    })
    @SecurityRequirement(name = "bearerAuth")   // shows 🔒 lock in Swagger UI
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getCurrentUser(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                authService.getCurrentUser(userDetails.getUsername()));
    }
//
//    // ── GET /admin/users ──────────────────────────────────────────────────────
//
//    @Operation(
//        summary     = "List all users (ADMIN only)",
//        description = "Returns a list of every registered user. " +
//                      "Requires JWT with ROLE_ADMIN."
//    )
//    @ApiResponses({
//        @ApiResponse(responseCode = "200", description = "User list returned"),
//        @ApiResponse(responseCode = "401", description = "Not authenticated"),
//        @ApiResponse(responseCode = "403", description = "Not ROLE_ADMIN")
//    })
//    @SecurityRequirement(name = "bearerAuth")
//    @GetMapping("/admin/users")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<List<UserProfileResponse>> getAllUsers() {
//        return ResponseEntity.ok(authService.getAllUsers());
//    }

    // ── GET /admin/ping ───────────────────────────────────────────────────────
//
//    @Operation(summary = "Admin ping — tests ROLE_ADMIN access")
//    @SecurityRequirement(name = "bearerAuth")
//    @GetMapping("/admin/ping")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<Map<String, String>> adminPing() {
//        return ResponseEntity.ok(Map.of("message", "Admin access confirmed"));
//    }
}
