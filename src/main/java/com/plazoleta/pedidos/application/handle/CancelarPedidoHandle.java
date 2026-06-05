package com.plazoleta.pedidos.application.handle;

import com.plazoleta.pedidos.application.dto.CancelarPedidoResponse;
import com.plazoleta.pedidos.domain.api.CancelarPedidoPort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class CancelarPedidoHandle {

    private final CancelarPedidoPort cancelarPedidoPort;

    public CancelarPedidoHandle(CancelarPedidoPort cancelarPedidoPort) {
        this.cancelarPedidoPort = cancelarPedidoPort;
    }

    public CancelarPedidoResponse cancelar(Long idPedido, Authentication authentication) {
        Long idCliente = (Long) authentication.getPrincipal();
        cancelarPedidoPort.cancelarPedido(idPedido, idCliente);
        return new CancelarPedidoResponse("Pedido cancelado exitosamente");
    }
}