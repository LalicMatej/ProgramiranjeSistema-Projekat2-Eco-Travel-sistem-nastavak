package org.raflab.vodiciservice.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.net.URI;
import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        return this::buildConfig;
    }

    private CorsConfiguration buildConfig(HttpServletRequest request) {
        String origin = request.getHeader("Origin");
        if (origin == null || !isAllowedOrigin(origin)) {
            return null;
        }

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(origin));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);
        return config;
    }

    private boolean isAllowedOrigin(String origin) {
        try {
            URI uri = URI.create(origin);
            String host = uri.getHost();
            int port = uri.getPort();
            return "localhost".equalsIgnoreCase(host) && port >= 8000;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
