package org.raflab.gatewayservice.filter;

import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
public class ResponseEnrichmentFilter implements GlobalFilter, Ordered {

   // private static final String START_TIME_ATTR = "requestStartTime";

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE - 1; // ide zadnji
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long startTime = System.currentTimeMillis();

        return chain.filter(exchange).doFinally(signalType -> {
            long elapsed = System.currentTimeMillis() - startTime;
            exchange.getResponse().getHeaders().add("X-Processing-Time", elapsed + "ms");
            String ecoStatus = elapsed < 100 ? "eco" : "normal";
            exchange.getResponse().getHeaders().add("X-Eco-Service-Status", ecoStatus);
            System.out.println("=== ResponseEnrichmentFilter ===");
            System.out.println("Processing time: " + elapsed + "ms");
            System.out.println("Headers added: " + exchange.getResponse().getHeaders());
        });
    }
//ovo dole nece da moze
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        exchange.getAttributes().put(START_TIME_ATTR, System.currentTimeMillis());
//
//        ServerHttpResponse originalResponse = exchange.getResponse();
//
//        //ovo sranje hvata kad se vraca response
//        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
//            @Override
//            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
//                Long startTime = exchange.getAttribute(START_TIME_ATTR);
//                System.out.println("StartTime je: " + startTime);
//                long elapsed = startTime != null ? System.currentTimeMillis() - startTime : 0;
//                System.out.printf("Elapsed time je : "+ elapsed);
//                getHeaders().add("X-Processing-Time", elapsed + "ms");
//                getHeaders().add("X-Eco-Service-Status", "active");
//                return super.writeWith(body);
//            }
//        };
//
//        return chain.filter(exchange.mutate().response(decoratedResponse).build());
//    }
}
