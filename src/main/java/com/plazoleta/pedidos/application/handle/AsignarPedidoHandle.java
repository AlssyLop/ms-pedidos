package com.plazoleta.pedidos.application.handle;

import com.plazoleta.pedidos.application.dto.AsignarPedidoResponse;
import com.plazoleta.pedidos.domain.api.AsignarPedidoPort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AsignarPedidoHandle {

    private final AsignarPedidoPort asignarPedidoPort;

    public AsignarPedidoHandle(AsignarPedidoPort asignarPedidoPort) {
        this.asignarPedidoPort = asignarPedidoPort;
    }

    public AsignarPedidoResponse asignar(Long idPedido, Authentication authentication) {
        Long idEmpleado = (Long) authentication.getPrincipal();
        asignarPedidoPort.asignarPedido(idPedido, idEmpleado);
        return new AsignarPedidoResponse("Pedido asignado exitosamente");
    }
}