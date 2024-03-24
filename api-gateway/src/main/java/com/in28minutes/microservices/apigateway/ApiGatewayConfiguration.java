package com.in28minutes.microservices.apigateway;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

// 配置一个API网关，用于根据请求的路径将请求路由到不同的微服务上，并可以通过过滤器进行一些预处理或转发请求。
@Configuration
public class ApiGatewayConfiguration {

    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {


        Function<PredicateSpec, Buildable<Route>> routeFunction
                = p -> p.path("/get") // lambda expression
                .filters(f -> f
                        .addRequestParameter("Param", "MyValue")
                        .addRequestHeader("MyHeader", "MyURI"))

                .uri("http://httpbin.org:80");  // when the request route is /get, it will match to the url provided here
        return builder.routes()
                        .route(routeFunction)
                .route(p -> p.path("/currency-exchange/**")
                        .uri("lb://currency-exchange"))
                .route(p -> p.path("/currency-conversion/**")
                        .uri("lb://currency-conversion"))
                .route(p -> p.path("/currency-conversion-feign/**")
                        .uri("lb://currency-conversion"))
                .route(p -> p.path("/currency-conversion-new/**")
                        .filters(f -> f.rewritePath(
                                "/currency-conversion-new/(?<segment>.*)",
                                "/currency-conversion-feign/${segment}"
                                ))
                        .uri("lb://currency-conversion"))
                        .build();
    }
}
