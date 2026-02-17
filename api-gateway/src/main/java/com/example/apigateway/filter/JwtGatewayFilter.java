package com.example.apigateway.filter;

import com.example.apigateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class JwtGatewayFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        // allow auth and swagger/actuator without token
        if (path.startsWith("/api/auth") || path.startsWith("/actuator") || path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            ServerHttpResponse resp = exchange.getResponse();
            resp.setStatusCode(HttpStatus.UNAUTHORIZED);
            resp.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            byte[] bytes = "{\"error\":\"Missing or invalid Authorization header\"}".getBytes(StandardCharsets.UTF_8);
            return resp.writeWith(Mono.just(resp.bufferFactory().wrap(bytes)));
        }

        String token = authHeader.substring(7);
        try {
            String username = jwtUtil.extractUsername(token);
            if (!jwtUtil.isTokenValid(token, username)) {
                ServerHttpResponse resp = exchange.getResponse();
                resp.setStatusCode(HttpStatus.UNAUTHORIZED);
                return resp.setComplete();
            }
            String tenant = jwtUtil.extractTenant(token);
            ServerHttpRequest mutated = exchange.getRequest().mutate()
                    .header("X-Tenant-ID", tenant == null ? "" : tenant)
                    .build();
            return chain.filter(exchange.mutate().request(mutated).build());
        } catch (Exception ex) {
            ServerHttpResponse resp = exchange.getResponse();
            resp.setStatusCode(HttpStatus.UNAUTHORIZED);
            return resp.setComplete();
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 10;
    }
}
