package com.example.cr.user.config;

import com.example.cr.user.filter.BearerTokenFilter;
import jakarta.servlet.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {
    @Bean
    public Filter bearerTokenFilter() {
        return new BearerTokenFilter();
    }
} 