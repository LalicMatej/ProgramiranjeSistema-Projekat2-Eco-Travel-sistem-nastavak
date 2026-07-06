package org.raflab.gatewayservice.filter;

import lombok.RequiredArgsConstructor;
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

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AuthFilter implements GlobalFilter, Ordered {

    private final WebClient webClient;

    @Override
    public int getOrder() {
        return -99;
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
        String apiKey = exchange.getRequest().getHeaders().getFirst("X-API-KEY");
        // ako nema api kljuc unauthorized - 403
        if(apiKey == null || apiKey.isBlank()){
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        return webClient.get()
                .uri("/api/auth/validate")
                .header("X-API-KEY", apiKey)
                .retrieve()
                .bodyToMono(ValidationResponseDto.class)
                .flatMap(response -> {
                        // spajamo roles sa ','
                        String roles = response.getRoles().stream()
                                .map(RoleDto::getRole)
                                .collect(Collectors.joining(","));

                        // dodajemo username i roles na request
                        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                                .header("X-Username", response.getUsername())
                                .header("X-User-Roles", roles)
                                .header("X-User-Role", roles)
                                .header("X-Authenticated-User", response.getUsername())
                                .build();

                        return chain.filter(exchange.mutate().request(mutatedRequest).build());
                    }
                )
                .onErrorResume(e->{
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                });
    }
}
