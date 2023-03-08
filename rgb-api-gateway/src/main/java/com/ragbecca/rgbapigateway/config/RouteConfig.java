package com.ragbecca.rgbapigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {
    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                //Player Service Route
                .route("rgb-player-service", p -> p
                        .path("/api/player/**")
                        .uri("lb://rgb-player-service"))
                //Character Service Route
                .route("rgb-character-service", p -> p
                        .path("/api/character/**")
                        .uri("lb://rgb-character-service"))
                //Discovery Server Route
                .route("rgb-discovery-server", p -> p
                        .path("/eureka/web")
                        .filters(f -> f.setPath("/"))
                        .uri("http://localhost:8761"))
                //Discovery Server Static Route
                .route("rgb-discovery-server-static", p -> p
                        .path("/eureka/**")
                        .uri("http://localhost:8761"))
                //Messaging Service Route
                .route("rgb-messaging-service", p -> p
                        .path("/ws-chat/**").or().path("/ws-chat")
                        .uri("lb:ws://rgb-messaging-service"))
                //Messaging Non-Websocket Service Route
                .route("rgb-messaging-sockjs-service", p -> p
                        .path("/api/chat/**")
                        .uri("lb://rgb-messaging-service"))
                .route("rgb-image-service", p -> p
                        .path("/api/image/**")
                        .uri("lb://rgb-image-service"))
                .build();
    }
}
