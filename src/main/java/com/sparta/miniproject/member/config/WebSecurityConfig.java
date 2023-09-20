package com.sparta.miniproject.member.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
    @Bean
    public Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> customizer() {
        // 인가 경로 설정
        return request -> request
                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/signup")).permitAll()
                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/login")).permitAll();
    }
}
