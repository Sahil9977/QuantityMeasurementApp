package com.app.quantitymeasurement.auth.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Utility component for JWT operations.
 *
 * Why no Lombok here?
 *   @Value injection happens AFTER construction, so @AllArgsConstructor
 *   would not receive the injected values. We use field injection instead.
 *
 * Uses JJWT 0.12.x API:
 *   - Jwts.builder()          → create token
 *   - Jwts.parser()           → parse + validate token
 *   - Keys.hmacShaKeyFor()    → derive SecretKey from Base64 secret
 */
@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    /** Base64-encoded 256-bit secret key, injected from application.properties */
    @Value("${app.jwt.secret}")
    private String jwtSecret;

    /** Token validity in milliseconds (default 24 h) */
    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;

    // ── Build a signed JWT ────────────────────────────────────────────────────

    public String generateToken(Authentication authentication) {
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        return buildToken(principal.getUsername());
    }

    public String generateTokenFromEmail(String email) {
        return buildToken(email);
    }

    private String buildToken(String email) {
        Date now    = new Date();
        Date expiry = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .subject(email)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(signingKey())
                .compact();
    }

    // ── Read claims ───────────────────────────────────────────────────────────

    public String getEmailFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    public long getExpirationMs() {
        return jwtExpirationMs;
    }

    // ── Validate ──────────────────────────────────────────────────────────────

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Empty JWT claims: {}", e.getMessage());
        }
        return false;
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey signingKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
