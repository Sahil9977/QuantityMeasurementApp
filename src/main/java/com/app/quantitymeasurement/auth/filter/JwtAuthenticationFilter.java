package com.app.quantitymeasurement.auth.filter;

import com.app.quantitymeasurement.auth.service.UserDetailsServiceImpl;
import com.app.quantitymeasurement.auth.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT Authentication Filter — runs ONCE per HTTP request.
 *
 * How it works:
 *  1. Read the "Authorization: Bearer <token>" header
 *  2. Validate the token using JwtUtil
 *  3. Load the user from DB via UserDetailsService
 *  4. Set the Authentication in the SecurityContext
 *  5. Continue the filter chain
 *
 * If the token is missing or invalid, the request continues unauthenticated
 * and Spring Security's access rules decide whether to reject it (401/403).
 *
 * extends OncePerRequestFilter → Spring guarantees this runs exactly once,
 * even with async dispatches or error pages.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest  request,
                                    HttpServletResponse response,
                                    FilterChain         filterChain)
            throws ServletException, IOException {

        try {
            String token = extractTokenFromRequest(request);

            if (StringUtils.hasText(token) && jwtUtil.validateToken(token)) {

                String email = jwtUtil.getEmailFromToken(token);
                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(email);

                // Build Authentication object and put it in the SecurityContext
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,                         // credentials not needed post-auth
                                userDetails.getAuthorities());

                auth.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(auth);
                log.debug("JWT authenticated user: {}", email);
            }

        } catch (Exception e) {
            log.error("Cannot set user authentication from JWT: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extracts the raw JWT string from the Authorization header.
     * Header format: "Authorization: Bearer eyJhbGci..."
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7);   // strip "Bearer "
        }
        return null;
    }
}
