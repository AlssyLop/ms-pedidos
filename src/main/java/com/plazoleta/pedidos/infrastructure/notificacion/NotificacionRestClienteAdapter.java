package com.plazoleta.pedidos.infrastructure.notificacion;

import com.plazoleta.pedidos.domain.spi.NotificacionClientePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class NotificacionRestClienteAdapter implements NotificacionClientePort {

    private final RestTemplate restTemplate;
    private final String msNotificacionesUrl;

    public NotificacionRestClienteAdapter(RestTemplate restTemplate,
                                          @Value("${ms-notificaciones.url}") String msNotificacionesUrl) {
        this.restTemplate = restTemplate;
        this.msNotificacionesUrl = msNotificacionesUrl;
    }

    @Override
    public boolean enviarNotificacion(Long idPedido, String celular, String mensaje) {
        try {
            Map<String, Object> request = Map.of(
                    "idPedido", idPedido,
                    "celular", celular,
                    "mensaje", mensaje
            );
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            ResponseEntity<Void> response = restTemplate.exchange(
                    msNotificacionesUrl + "/notificaciones/enviar",
                    HttpMethod.POST, entity, Void.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }
}
