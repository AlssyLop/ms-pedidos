package com.plazoleta.pedidos.application.handle;

import com.plazoleta.pedidos.application.dto.NotificarPedidoListoResponse;
import com.plazoleta.pedidos.domain.api.NotificarPedidoListoPort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class NotificarPedidoListoHandle {

    private final NotificarPedidoListoPort notificarPedidoListoPort;

    public NotificarPedidoListoHandle(NotificarPedidoListoPort notificarPedidoListoPort) {
        this.notificarPedidoListoPort = notificarPedidoListoPort;
    }

    public NotificarPedidoListoResponse notificar(Long idPedido, Authentication authentication) {
        Long idEmpleado = (Long) authentication.getPrincipal();
        String mensaje = notificarPedidoListoPort.notificar(idPedido, idEmpleado);
        return new NotificarPedidoListoResponse(mensaje);
    }
}
