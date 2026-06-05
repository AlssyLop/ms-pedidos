package com.plazoleta.pedidos.infrastructure.usuario;

import com.plazoleta.pedidos.domain.model.value.ClienteInfo;
import com.plazoleta.pedidos.domain.spi.ClienteValidacionPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class UsuarioRestPedidosAdapter implements ClienteValidacionPort {

    private final RestTemplate restTemplate;
    private final String msUsuariosUrl;

    public UsuarioRestPedidosAdapter(RestTemplate restTemplate,
                                     @Value("${ms-usuarios.url}") String msUsuariosUrl) {
        this.restTemplate = restTemplate;
        this.msUsuariosUrl = msUsuariosUrl;
    }

    @Override
    public Optional<ClienteInfo> obtenerCliente(Long idCliente) {
        try {
            ClienteInfoResponse response = restTemplate.getForObject(
                    msUsuariosUrl + "/usuarios/" + idCliente,
                    ClienteInfoResponse.class
            );
            return Optional.of(new ClienteInfo(idCliente, response.getNombre(), "+0000000000"));
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }
}