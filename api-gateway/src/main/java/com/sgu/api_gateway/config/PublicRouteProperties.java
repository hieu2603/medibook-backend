package com.sgu.api_gateway.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "gateway")
@Getter
@Setter
public class PublicRouteProperties {

    private List<RouteRule> publicRoutes;

    @Getter
    @Setter
    public static class RouteRule {
        private String path;
        private List<HttpMethod> methods;
    }
}
