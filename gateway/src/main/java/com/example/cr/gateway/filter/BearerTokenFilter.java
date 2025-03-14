package com.example.cr.gateway.filter;

import com.example.cr.common.util.CustomJWTUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class BearerTokenFilter implements GlobalFilter, Ordered {
    private static final Logger log = LoggerFactory.getLogger(BearerTokenFilter.class);

    @Autowired
    CustomJWTUtils customJWTUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        if (path.endsWith("/login")
                || path.endsWith("/register")
                || path.endsWith("/send-code")
                || path.contains("/test")) {
            log.info("不需要登录验证 {}", path);
            return chain.filter(exchange);
        }

        // 检查 http 请求头中是否带有 Authorization: Bearer token
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // 解析 "Bearer " 后面的 token
            token = authHeader.substring(7);
        }

        // 判断 token 是否为空
        if (token == null || token.isEmpty()) {
            log.info("token 为空，拦截该请求 {}", path);
            // 直接返回 401 不再往后走了
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // 验证 token 是否有效
        log.info("解析到请求头的 token={}", token);
        boolean isValidated = false;
        try {
            isValidated = customJWTUtils.validate(token);
        } catch (Exception e) {
            log.error("token={}, 解析异常：{}", token, e.getMessage());
        }
        if (!isValidated) {
            log.error("token 无效，请重新登录 {}", token);
            // 直接返回 401 不再往后走了
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
