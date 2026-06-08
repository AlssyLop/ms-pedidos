package com.plazoleta.pedidos.infrastructure.restaurante;

import com.plazoleta.pedidos.domain.model.PlatoInfo;
import com.plazoleta.pedidos.domain.spi.PropietarioRestaurantePort;
import com.plazoleta.pedidos.domain.spi.RestauranteValidacionPort;
import com.plazoleta.pedidos.infrastructure.restaurante.dto.PlatoInfoResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class RestauranteRestPedidosAdapter implements RestauranteValidacionPort, PropietarioRestaurantePort {

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
    public List<PlatoInfo> obtenerInfoPlatos(Long idRestaurante, List<Long> idsPlatos) {
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<List<Long>> request = new HttpEntity<>(idsPlatos, headers);
            ResponseEntity<PlatoInfoResponse[]> response = restTemplate.exchange(
                    msRestaurantesUrl + "/restaurantes/" + idRestaurante + "/platos/info",
                    HttpMethod.POST, request, PlatoInfoResponse[].class);
            return Arrays.stream(response.getBody())
                    .map(r -> new PlatoInfo(r.getIdPlato(), r.getNombrePlato(), r.isActivo()))
                    .toList();
        } catch (HttpClientErrorException e) {
            return List.of();
        }
    }

    @Override
    public Optional<Long> obtenerIdRestauranteDelPropietario(Long idPropietario) {
        try {
            ResponseEntity<RestauranteInfoResponse> response = restTemplate.exchange(
                    msRestaurantesUrl + "/restaurantes/propietario/" + idPropietario,
                    HttpMethod.GET, HttpEntity.EMPTY, RestauranteInfoResponse.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return Optional.of(response.getBody().getId());
            }
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
        return Optional.empty();
    }
}
