package org.example.routes;

import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;

@Configuration
public class Routes {
    @Bean
    public RouterFunction<ServerResponse> workoutServiceRoute() {
        return RouterFunctions.route()
                .path("/api/workout-service/**",
                        builder -> builder
                                .route(RequestPredicates.all(), HandlerFunctions.http("http://localhost:8081")))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("workoutServiceCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> scoringServiceRoute() {
        return RouterFunctions.route()
                .path("/api/scoring-service/**",
                        builder -> builder
                                .route(RequestPredicates.all(), HandlerFunctions.http("http://localhost:8087")))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("scoringServiceCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> referralServiceRoute() {
        return RouterFunctions.route()
                .path("/api/referral-service/**",
                        builder -> builder
                                .route(RequestPredicates.all(), HandlerFunctions.http("http://localhost:8082")))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("referralServiceCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> notificationServiceRoute() {
        return RouterFunctions.route()
                .path("/api/notification-service/**",
                        builder -> builder
                                .route(RequestPredicates.all(), HandlerFunctions.http("http://localhost:8084")))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("notificationServiceCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .build();
    }


    @Bean
    public RouterFunction<ServerResponse> fallbackRoute() {
        return route("fallbackRoute")
                .GET("/fallbackRoute", request -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("Service Unavailable, please try again later"))
                .build();
    }

//    SWAGGER ROUTES

    @Bean
    public RouterFunction<ServerResponse> workoutServiceSwaggerRoute() {
        return route("workout_service_swagger")
                .route(RequestPredicates.path("/aggregate/workout-service/v3/api-docs"), HandlerFunctions.http("http://localhost:8081"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("workoutServiceSwaggerCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .filter(setPath("/api-docs"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> referralServiceSwaggerRoute() {
        return route("referral_service_swagger")
                .route(RequestPredicates.path("/aggregate/referral-service/v3/api-docs"), HandlerFunctions.http("http://localhost:8081"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("referralServiceSwaggerCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .filter(setPath("/api-docs"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> notificationServiceSwaggerRoute() {
        return route("notification_service_swagger")
                .route(RequestPredicates.path("/aggregate/notification-service/v3/api-docs"), HandlerFunctions.http("http://localhost:8081"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("notificationServiceSwaggerCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .filter(setPath("/api-docs"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> scoringServiceSwaggerRoute() {
        return route("scoring_service_swagger")
                .route(RequestPredicates.path("/aggregate/scoring-service/v3/api-docs"), HandlerFunctions.http("http://localhost:8081"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("scoringServiceSwaggerCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .filter(setPath("/api-docs"))
                .build();
    }

}
