package com.plazoleta.pedidos.application.handle;

import com.plazoleta.pedidos.application.dto.PedidoRequest;
import com.plazoleta.pedidos.application.dto.PedidoResponse;
import com.plazoleta.pedidos.application.factory.PedidoFactory;
import com.plazoleta.pedidos.domain.api.CrearPedidoPort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class CrearPedidoHandle {

    private final CrearPedidoPort crearPedidoPort;
    private final PedidoFactory pedidoFactory;

    public CrearPedidoHandle(CrearPedidoPort crearPedidoPort, PedidoFactory pedidoFactory) {
        this.crearPedidoPort = crearPedidoPort;
        this.pedidoFactory = pedidoFactory;
    }

    public PedidoResponse crearPedido(PedidoRequest request, Authentication authentication) {
        Long idCliente = (Long) authentication.getPrincipal();
        var domain = pedidoFactory.toDomain(request, idCliente, "Cliente", "0000000000");
        crearPedidoPort.crearPedido(domain);
        return new PedidoResponse("Pedido realizado exitosamente");
    }
}
