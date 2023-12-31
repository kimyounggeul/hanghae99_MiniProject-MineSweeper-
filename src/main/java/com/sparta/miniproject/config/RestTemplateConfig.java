package com.sparta.miniproject.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class RestTemplateConfig  {
    private final ResponseErrorHandler responseErrorHandler;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        new DefaultResponseErrorHandler();
        return restTemplateBuilder
                // RestTemplate 으로 외부 API 호출 시 일정 시간이 지나도 응답이 없을 때
                // 무한 대기 상태 방지를 위해 강제 종료 설정
                .setConnectTimeout(Duration.ofSeconds(5)) // 5초
                .setReadTimeout(Duration.ofSeconds(5)) // 5초

                // 에러 로깅을 위한 Error Handler
                .errorHandler(responseErrorHandler)
                .build();
    }
}