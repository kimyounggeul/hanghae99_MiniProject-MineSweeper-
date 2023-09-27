package com.sparta.miniproject.common.config;

import com.sparta.miniproject.common.filter.JwtAuthorizationFilter;
import com.sparta.miniproject.common.filter.ResponseEncodingFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@RequiredArgsConstructor
public class ResponseEncodingConfig {
    private final ResponseEncodingFilter responseEncodingFilter;

    @Bean
    public Customizer<HttpSecurity> responseEncoding() {
        return http -> http.addFilterBefore(responseEncodingFilter, JwtAuthorizationFilter.class);
    }
}
