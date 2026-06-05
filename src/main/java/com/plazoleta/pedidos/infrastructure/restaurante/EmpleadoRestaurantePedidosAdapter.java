package com.plazoleta.pedidos.infrastructure.restaurante;

import com.plazoleta.pedidos.domain.spi.EmpleadoRestaurantePedidosPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class EmpleadoRestaurantePedidosAdapter implements EmpleadoRestaurantePedidosPort {

    private final RestTemplate restTemplate;
    private final String msRestaurantesUrl;

    public EmpleadoRestaurantePedidosAdapter(RestTemplate restTemplate,
                                             @Value("${ms-restaurantes.url}") String msRestaurantesUrl) {
        this.restTemplate = restTemplate;
        this.msRestaurantesUrl = msRestaurantesUrl;
    }

    @Override
    public Optional<Long> obtenerIdRestauranteDelEmpleado(Long idEmpleado) {
        try {
            EmpleadoRestauranteInfoResponse response = restTemplate.getForObject(
                    msRestaurantesUrl + "/restaurantes/empleado/" + idEmpleado,
                    EmpleadoRestauranteInfoResponse.class
            );
            return Optional.ofNullable(response).map(r -> r.getIdRestaurante());
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }
}