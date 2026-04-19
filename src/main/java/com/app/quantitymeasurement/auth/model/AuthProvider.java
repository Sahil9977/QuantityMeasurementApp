package com.app.quantitymeasurement.auth.model;

/**
 * Identifies HOW a user authenticated.
 * LOCAL  = email + password (JWT)
 * GOOGLE = Google OAuth2 login
 */
public enum AuthProvider {
    LOCAL,
    GOOGLE
}
