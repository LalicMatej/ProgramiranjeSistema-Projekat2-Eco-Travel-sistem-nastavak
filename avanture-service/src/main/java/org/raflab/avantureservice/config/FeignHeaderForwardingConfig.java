package org.raflab.avantureservice.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

@Configuration
public class FeignHeaderForwardingConfig {

    private static final List<String> HEADERS_TO_FORWARD = List.of(
            "X-API-KEY",
            "X-User-Role",
            "X-Authenticated-User",
            "X-Trace-Id"
    );

    @Bean
    public RequestInterceptor requestHeaderForwardingInterceptor() {
        return template -> {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs == null) {
                return;
            }
            HttpServletRequest request = attrs.getRequest();
            for (String header : HEADERS_TO_FORWARD) {
                String value = request.getHeader(header);
                if (value != null && !value.isBlank()) {
                    template.header(header, value);
                }
            }
        };
    }
}
