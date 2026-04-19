package com.app.quantitymeasurement.auth.service;

import com.app.quantitymeasurement.auth.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

/**
 * Called by Spring Security after a successful Google OAuth2 login.
 *
 * What happens after Google redirects the user back to our app?
 *  1. Spring Security has authenticated the user via CustomOAuth2UserService
 *  2. This handler fires
 *  3. We generate a JWT for the user
 *  4. We redirect the browser to the frontend callback URL with the token
 *     as a query parameter: http://localhost:3000/oauth2/callback?token=xxx
 *
 * The frontend reads the token from the URL and stores it in localStorage
 * for subsequent API calls.
 *
 * extends SimpleUrlAuthenticationSuccessHandler → provides redirect helpers
 */
@Component
public class OAuth2AuthenticationSuccessHandler
        extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger log =
            LoggerFactory.getLogger(OAuth2AuthenticationSuccessHandler.class);

    @Autowired
    private JwtUtil jwtUtil;

    /** Where to redirect the browser after OAuth2 success (configured in properties) */
    @Value("${app.oauth2.redirect-uri:http://localhost:5173/oauth2/callback}")
    private String redirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest  request,
                                        HttpServletResponse response,
                                        Authentication      authentication)
            throws IOException {

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getUser().getEmail();

        // Generate JWT
        String token = jwtUtil.generateTokenFromEmail(email);
        log.info("OAuth2 login success for {}, issuing JWT and redirecting", email);

        // Build redirect URL: <frontend>/oauth2/callback?token=<jwt>
        String targetUrl = UriComponentsBuilder
                .fromUriString(redirectUri)
                .queryParam("token", token)
                .build().toUriString();

        clearAuthenticationAttributes(request);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
