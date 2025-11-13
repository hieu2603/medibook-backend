package com.sgu.api_gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sgu.api_gateway.config.PublicRouteProperties;
import com.sgu.api_gateway.dto.common.ErrorResponse;
import com.sgu.api_gateway.utils.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class JwtAuthFilter extends AbstractGatewayFilterFactory<JwtAuthFilter.Config> {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PublicRouteProperties routeProperties;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public JwtAuthFilter(JwtUtil jwtUtil1, PublicRouteProperties routeProperties) {
        super(Config.class);
        this.jwtUtil = jwtUtil1;
        this.routeProperties = routeProperties;
    }

    public static class Config {
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath();
            HttpMethod method = exchange.getRequest().getMethod();

            boolean isPublic = routeProperties.getPublicRoutes().stream()
                    .anyMatch(rule ->
                            pathMatcher.match(rule.getPath(), path) && rule.getMethods().contains(method)
                    );

            if (isPublic) {
                return chain.filter(exchange);
            }

            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return writeErrorResponse(exchange, "Missing or invalid Bearer token in the 'Authorization' header");
            }

            String token = authHeader.substring(7);
            try {
                jwtUtil.validateToken(token);
            } catch (Exception e) {
                return writeErrorResponse(exchange, e.getMessage());
            }

            String id = jwtUtil.extractSubject(token);
            String email = jwtUtil.extractEmail(token);
            String role = jwtUtil.extractRole(token);

            ServerHttpRequest mutated = exchange.getRequest()
                    .mutate()
                    .header("X-User-Id", id)
                    .header("X-User-Email", email)
                    .header("X-User-Role", role)
                    .build();

            return chain.filter(exchange.mutate().request(mutated).build());
        });
    }

    private Mono<Void> writeErrorResponse(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("Unauthorized")
                .message(message)
                .build();

        try {
            byte[] bytes = objectMapper.writeValueAsBytes(errorResponse);
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
            return exchange.getResponse().writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            byte[] bytes = ("{\"error\":\"Failed to serialize error response\"}")
                    .getBytes(StandardCharsets.UTF_8);
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
            return exchange.getResponse().writeWith(Mono.just(buffer));
        }
    }
}
