package com.plazoleta.pedidos.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class RestTemplateConfig {

    private final JwtForwardingInterceptor jwtForwardingInterceptor;

    public RestTemplateConfig(JwtForwardingInterceptor jwtForwardingInterceptor) {
        this.jwtForwardingInterceptor = jwtForwardingInterceptor;
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(List.of(jwtForwardingInterceptor));
        return restTemplate;
    }
}
