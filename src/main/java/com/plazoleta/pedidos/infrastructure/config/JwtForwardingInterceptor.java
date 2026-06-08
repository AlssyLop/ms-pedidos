package com.plazoleta.pedidos.infrastructure.config;

import com.plazoleta.pedidos.infrastructure.security.jwt.JwtTokenHolder;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtForwardingInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        String token = JwtTokenHolder.getToken();
        if (token != null && !token.isBlank()) {
            request.getHeaders().set("Authorization", "Bearer " + token);
        }
        return execution.execute(request, body);
    }
}
