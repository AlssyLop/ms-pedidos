package com.plazoleta.pedidos.application.handle;

import com.plazoleta.pedidos.application.dto.EntregarPedidoRequest;
import com.plazoleta.pedidos.application.dto.EntregarPedidoResponse;
import com.plazoleta.pedidos.domain.api.EntregarPedidoPort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class EntregarPedidoHandle {

    private final EntregarPedidoPort entregarPedidoPort;

    public EntregarPedidoHandle(EntregarPedidoPort entregarPedidoPort) {
        this.entregarPedidoPort = entregarPedidoPort;
    }

    public EntregarPedidoResponse entregar(Long idPedido, EntregarPedidoRequest request, Authentication authentication) {
        Long idEmpleado = (Long) authentication.getPrincipal();
        entregarPedidoPort.entregarPedido(idPedido, idEmpleado, request.getPin());
        return new EntregarPedidoResponse("Pedido entregado exitosamente");
    }
}