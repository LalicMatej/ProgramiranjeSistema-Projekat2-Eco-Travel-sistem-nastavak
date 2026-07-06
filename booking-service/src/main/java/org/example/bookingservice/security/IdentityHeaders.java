package org.example.bookingservice.security;

public final class IdentityHeaders {

    public static final String REQUEST_ID = "X-Request-Id";
    public static final String AUTHENTICATED_USER = "X-Authenticated-User";
    public static final String USER_ROLES = "X-User-Roles";

    private IdentityHeaders() {
    }
}
