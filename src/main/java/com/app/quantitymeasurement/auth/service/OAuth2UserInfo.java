package com.app.quantitymeasurement.auth.service;

import java.util.Map;

/**
 * Normalises the user-info attributes returned by Google OAuth2.
 *
 * Google's /userinfo endpoint returns a flat JSON map. The keys differ
 * between providers (e.g. Google uses "sub" for the unique user ID, while
 * other providers use "id"). This class hides that detail from the rest
 * of the application.
 *
 * Why no Lombok? — final fields + simple constructor is cleaner here;
 * no setters needed because the attributes are read-only after creation.
 */
public class OAuth2UserInfo {

    private final Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    /** Google's unique user ID (the "sub" claim in the ID token) */
    public String getId() {
        return (String) attributes.get("sub");
    }

    public String getName() {
        return (String) attributes.get("name");
    }

    public String getEmail() {
        return (String) attributes.get("email");
    }

    /** Profile picture URL provided by Google */
    public String getImageUrl() {
        return (String) attributes.get("picture");
    }

    public boolean isEmailVerified() {
        Object verified = attributes.get("email_verified");
        if (verified instanceof Boolean b) return b;
        if (verified instanceof String  s) return Boolean.parseBoolean(s);
        return false;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
