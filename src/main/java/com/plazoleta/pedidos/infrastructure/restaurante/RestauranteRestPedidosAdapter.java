package com.plazoleta.pedidos.infrastructure.restaurante;

import com.plazoleta.pedidos.domain.spi.RestauranteValidacionPort;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class RestauranteRestPedidosAdapter implements RestauranteValidacionPort {

    private final RestTemplate restTemplate;
    private final String msRestaurantesUrl;

    public RestauranteRestPedidosAdapter(RestTemplate restTemplate,
                                         @Value("${ms-restaurantes.url}") String msRestaurantesUrl) {
        this.restTemplate = restTemplate;
        this.msRestaurantesUrl = msRestaurantesUrl;
    }

    @Override
    public boolean existsById(Long idRestaurante) {
        try {
            ResponseEntity<Void> response = restTemplate.exchange(
                    msRestaurantesUrl + "/restaurantes/" + idRestaurante,
                    HttpMethod.GET, HttpEntity.EMPTY, Void.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        }
    }

    @Override
    public List<Long> validarPlatosPertenecenARestaurante(Long idRestaurante, List<Long> idsPlatos) {
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<List<Long>> request = new HttpEntity<>(idsPlatos, headers);
            ResponseEntity<Long[]> response = restTemplate.exchange(
                    msRestaurantesUrl + "/restaurantes/" + idRestaurante + "/platos/validar-pertenencia",
                    HttpMethod.POST, request, Long[].class);
            return Arrays.asList(response.getBody());
        } catch (HttpClientErrorException e) {
            return List.of();
        }
    }

    @Override
    public List<Long> validarPlatosActivos(List<Long> idsPlatos) {
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<List<Long>> request = new HttpEntity<>(idsPlatos, headers);
            ResponseEntity<Long[]> response = restTemplate.exchange(
                    msRestaurantesUrl + "/platos/validar-activos",
                    HttpMethod.POST, request, Long[].class);
            return Arrays.asList(response.getBody());
        } catch (HttpClientErrorException e) {
            return List.of();
        }
    }
}
