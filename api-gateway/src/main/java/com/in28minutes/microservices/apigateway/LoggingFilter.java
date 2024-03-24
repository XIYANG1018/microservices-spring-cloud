package com.in28minutes.microservices.apigateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

//这段代码实现了一个全局过滤器（LoggingFilter），它的业务目的是在请求进入 API 网关时记录请求的路径信息。
// 具体来说，它通过 SLF4J 的 Logger 将请求路径信息以日志的形式输出到日志系统中。
@Component
public class LoggingFilter implements GlobalFilter {
    // create a logger
    private final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // log the path of the request
        logger.info("Path of the request received -> {}", exchange.getRequest().getPath());
        return chain.filter(exchange);
    }
}
