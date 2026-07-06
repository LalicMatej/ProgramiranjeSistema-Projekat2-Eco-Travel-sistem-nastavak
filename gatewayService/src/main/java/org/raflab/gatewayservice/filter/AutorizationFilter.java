package org.raflab.gatewayservice.filter;

import lombok.RequiredArgsConstructor;
import org.raflab.gatewayservice.config.RouteSecConfig;
import org.raflab.gatewayservice.dtos.RoleDto;
import org.raflab.gatewayservice.dtos.ValidationResponseDto;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AutorizationFilter implements GlobalFilter, Ordered {

    private final RouteSecConfig routeSecurityConfig;

    @Override
    public int getOrder() {
        return -98;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        // skip auth endpointove
        if(path.startsWith("/api/auth")){
            return chain.filter(exchange);
        }
        if (path.contains("/v3/api-docs") || path.contains("/swagger-ui") || path.contains("/auth")) {
            return chain.filter(exchange);
        }
        String method = String.valueOf(exchange.getRequest().getMethod());
        String username = exchange.getRequest().getHeaders().getFirst("X-Username");
        String rolesHeader = exchange.getRequest().getHeaders().getFirst("X-User-Roles");
        List<String> userRoles = rolesHeader != null
                ? Arrays.asList(rolesHeader.split(","))
                : List.of();

        if(!routeSecurityConfig.isAllowed(method,path,userRoles)){
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }


}
