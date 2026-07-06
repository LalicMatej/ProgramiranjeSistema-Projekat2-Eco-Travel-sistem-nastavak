package org.raflab.gatewayservice.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

// TraceFilter uklonjen - OTel auto-konfiguracija (Spring Boot 3.3.5) kreira
// TracingWebFilter koji automatski propagira W3C traceparent header downstream
public class TraceFilter implements GlobalFilter, Ordered {

    @Override
    public int getOrder() { return -100; }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return Mono.defer(() -> chain.filter(exchange));
    }
}