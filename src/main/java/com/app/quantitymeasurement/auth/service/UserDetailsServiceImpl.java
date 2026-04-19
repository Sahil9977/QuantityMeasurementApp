package com.app.quantitymeasurement.auth.service;

import com.app.quantitymeasurement.auth.model.User;
import com.app.quantitymeasurement.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Tells Spring Security how to load a user from the database given an email.
 *
 * Spring Security calls loadUserByUsername() during:
 *   1. JWT filter — to rebuild the Authentication object from the token
 *   2. Login — DaoAuthenticationProvider calls this before checking password
 *
 * @Service  → registers as a Spring bean (component scanning picks it up)
 * @Transactional(readOnly=true) → performance optimization: read-only DB tx
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "No user found with email: " + email));

        return new CustomUserDetails(user);
    }
}
