package com.app.quantitymeasurement.auth.service;

import com.app.quantitymeasurement.auth.model.AuthProvider;
import com.app.quantitymeasurement.auth.model.User;
//import com.app.quantitymeasurement.auth.model.UserRole;
import com.app.quantitymeasurement.auth.repository.UserRepository;
import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Handles the second leg of Google OAuth2 login.
 *
 * Flow:
 *  1. User clicks "Login with Google"
 *  2. Spring redirects to Google's consent screen
 *  3. Google redirects back with an authorization code
 *  4. Spring exchanges the code for an access token
 *  5. Spring calls THIS service with the token → we load the user info
 *  6. We register (first login) or update (returning user) in our DB
 *  7. We return a CustomOAuth2User so Spring Security can complete authentication
 *
 * Extends DefaultOAuth2UserService → delegates the actual HTTP call to Google
 * to the parent, then post-processes the result.
 */
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private static final Logger log = LoggerFactory.getLogger(CustomOAuth2UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {

        // Step 1 — fetch attributes from Google's /userinfo endpoint
        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserInfo userInfo = new OAuth2UserInfo(oAuth2User.getAttributes());

        log.info("Google OAuth2 login for email: {}", userInfo.getEmail());

        if (userInfo.getEmail() == null || userInfo.getEmail().isBlank()) {
            throw new OAuth2AuthenticationException(
                    "Email not provided by Google OAuth2");
        }

        // Step 2 — register or update the user in our database
        User user = registerOrUpdateUser(userInfo);

        // Step 3 — return a CustomOAuth2User that implements both
        //           OAuth2User (for Spring's OAuth2 pipeline) and
        //           UserDetails (for our JWT pipeline)
        return new CustomOAuth2User(user, oAuth2User.getAttributes());
    }

    private User registerOrUpdateUser(OAuth2UserInfo info) {

        // Case A: user already registered via Google — just update their profile
        Optional<User> existingByProvider =
                userRepository.findByProviderIdAndProvider(
                        info.getId(), AuthProvider.GOOGLE);

        if (existingByProvider.isPresent()) {
            User user = existingByProvider.get();
            user.setName(info.getName());
            user.setImageUrl(info.getImageUrl());
            user.setEmailVerified(info.isEmailVerified());
            return userRepository.save(user);
        }

        // Case B: same email already registered locally — link the Google account
        Optional<User> existingByEmail =
                userRepository.findByEmail(info.getEmail());

        if (existingByEmail.isPresent()) {
            User user = existingByEmail.get();
            if (user.getProvider() != AuthProvider.GOOGLE) {
                log.warn("Email {} registered via {}, now also via Google — linking accounts",
                        info.getEmail(), user.getProvider());
                // We allow linking: update the provider info
                user.setProvider(AuthProvider.GOOGLE);
                user.setProviderId(info.getId());
                user.setImageUrl(info.getImageUrl());
                user.setEmailVerified(info.isEmailVerified());
                return userRepository.save(user);
            }
        }

        // Case C: brand-new user — create their account
        User newUser = User.builder()
                .name(info.getName())
                .email(info.getEmail())
                .provider(AuthProvider.GOOGLE)
                .providerId(info.getId())
                .imageUrl(info.getImageUrl())
                .emailVerified(info.isEmailVerified())
//                .role(UserRole.ROLE_USER)
                .passwordHash(null)   // no password for Google users
                .build();

        log.info("Registering new Google user: {}", info.getEmail());
        return userRepository.save(newUser);
    }
}
