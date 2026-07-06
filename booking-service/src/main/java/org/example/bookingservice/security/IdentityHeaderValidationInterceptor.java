package org.example.bookingservice.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.bookingservice.exception.BadRequestException;
import org.example.bookingservice.exception.UnauthorizedException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class IdentityHeaderValidationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String user = request.getHeader(IdentityHeaders.AUTHENTICATED_USER);
        String roles = request.getHeader(IdentityHeaders.USER_ROLES);
        String requestId = request.getHeader(IdentityHeaders.REQUEST_ID);

        if ((hasText(user) && !hasText(roles)) || (!hasText(user) && hasText(roles))) {
            throw new BadRequestException("Authenticated user and roles headers must be provided together.");
        }

        if (user != null && !hasText(user)) {
            throw new BadRequestException("X-Authenticated-User header must not be blank.");
        }

        if (roles != null && !hasText(roles)) {
            throw new BadRequestException("X-User-Roles header must not be blank.");
        }

        if (requestId != null && !hasText(requestId)) {
            throw new BadRequestException("X-Request-Id header must not be blank.");
        }

        if (request.getRequestURI().startsWith("/api/bookings/internal/")
                && (!hasText(user) || !hasText(roles))) {
            throw new UnauthorizedException("Internal booking request is missing authenticated user headers.");
        }

        return true;
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
