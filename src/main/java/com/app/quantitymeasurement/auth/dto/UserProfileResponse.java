package com.app.quantitymeasurement.auth.dto;

import com.app.quantitymeasurement.auth.model.AuthProvider;
import com.app.quantitymeasurement.auth.model.User;
//import com.app.quantitymeasurement.auth.model.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Returned by GET /api/v1/auth/me — the currently logged-in user's profile.
 * Uses Lombok @Builder for easy construction from a User entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Current user's profile")
public class UserProfileResponse {

    @Schema(description = "Database ID", example = "1")
    private Long id;

    @Schema(description = "Full name", example = "Rahul Sharma")
    private String name;

    @Schema(description = "Email address", example = "rahul@example.com")
    private String email;

//    @Schema(description = "Role assigned to this user")
//    private UserRole role;

    @Schema(description = "Authentication provider: LOCAL or GOOGLE")
    private AuthProvider provider;

    @Schema(description = "Profile picture URL (Google users only)")
    private String imageUrl;

    @Schema(description = "Whether the email address has been verified")
    private boolean emailVerified;

    /** Static factory — converts User entity → this DTO */
    public static UserProfileResponse fromUser(User user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
//                .role(user.getRole())
                .provider(user.getProvider())
                .imageUrl(user.getImageUrl())
                .emailVerified(user.isEmailVerified())
                .build();
    }
}

