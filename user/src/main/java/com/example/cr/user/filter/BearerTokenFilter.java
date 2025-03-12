package com.example.cr.user.filter;

import com.example.cr.common.util.CustomJWTUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class BearerTokenFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(BearerTokenFilter.class);

    @Resource
    CustomJWTUtils customJWTUtils;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = httpRequest.getRequestURI();

        if (requestURI.endsWith("/login")
                || requestURI.endsWith("/register")
                || requestURI.endsWith("/send-code")
                || requestURI.contains("/test")) {
            log.info("不需要登录验证 {}", requestURI);
            chain.doFilter(request, response);
            return;
        }

        // 获取 Authorization 请求头的值
        String authorizationHeader = httpRequest.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // 解析 "Bearer " 后面的 token
            token = authorizationHeader.substring(7);
        }

        // 判断 token 是否为空
        if (token == null || token.isEmpty()) {
            log.info("token 为空，拦截请求 {}", request);
            // 直接返回 401 不再往后走了
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 验证 token 是否有效
        log.info("解析到请求头的 token={}", token);
        boolean isValidated = false;
        try {
            isValidated = customJWTUtils.validate(token);
        } catch (Exception e) {
            log.warn("token={}, 解析异常：{}", token, e.getMessage());
        }
        if (!isValidated) {
            log.error("token 无效，请重新登录 {}", token);
            // 直接返回 401 不再往后走了
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 一切正常
        chain.doFilter(request, response);
    }
} 