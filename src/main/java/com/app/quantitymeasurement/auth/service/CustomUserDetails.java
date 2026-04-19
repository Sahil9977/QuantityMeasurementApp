package com.app.quantitymeasurement.auth.service;

import com.app.quantitymeasurement.auth.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Adapts our User entity to Spring Security's UserDetails interface.
 *
 * Spring Security's authentication pipeline needs a UserDetails object.
 * This class wraps our User entity and exposes the fields Spring Security
 * needs: username (email), password, and authorities (roles).
 *
 * Why no Lombok here?
 *   UserDetails has methods like isAccountNonExpired() that would conflict
 *   with Lombok-generated methods, so we implement them explicitly.
 */
public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    /** Expose the underlying User entity (used in controllers via SecurityContext) */
    public User getUser() {
        return user;
    }

    // ── UserDetails interface ─────────────────────────────────────────────────

    @Override
    public String getUsername() {
        // We use EMAIL as the username (unique identifier)
        return user.getEmail();
    }

    @Override
    public String getPassword() {
        // Returns bcrypt hash — null for Google OAuth2 users
        return user.getPasswordHash();
    }

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        // UserRole enum values are already "ROLE_USER" / "ROLE_ADMIN"
//        return Collections.singletonList(
//                new SimpleGrantedAuthority(user.getRole().name()));
//    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override public boolean isAccountNonExpired()  { return true; }
    @Override public boolean isAccountNonLocked()   { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled()            { return true; }
}
