package com.app.quantitymeasurement.auth.service;

import com.app.quantitymeasurement.auth.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Bridges OAuth2User (Spring OAuth2 pipeline) with UserDetails (JWT pipeline).
 *
 * After Google OAuth2 completes, we need to issue a JWT.
 * To do that we need access to our User entity inside the
 * OAuth2AuthenticationSuccessHandler. This class wraps the User entity
 * and implements both interfaces so it fits both pipelines cleanly.
 *
 * Implements:
 *   OAuth2User   → used by Spring's OAuth2 login completion
 *   UserDetails  → used by our JWT issuance code
 */
public class CustomOAuth2User implements OAuth2User, UserDetails {

    private final User                user;
    private final Map<String, Object> attributes;

    public CustomOAuth2User(User user, Map<String, Object> attributes) {
        this.user       = user;
        this.attributes = attributes;
    }

    public User getUser() { return user; }

    // ── OAuth2User ────────────────────────────────────────────────────────────

    @Override
    public Map<String, Object> getAttributes() { return attributes; }

    @Override
    public String getName() {
        // OAuth2User.getName() should return the principal name — we use email
        return user.getEmail();
    }

    // ── UserDetails ───────────────────────────────────────────────────────────

    @Override
    public String getUsername() { return user.getEmail(); }

    @Override
    public String getPassword() { return user.getPasswordHash(); }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override public boolean isAccountNonExpired()     { return true; }
    @Override public boolean isAccountNonLocked()      { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled()               { return true; }
}
