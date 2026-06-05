package com.plazoleta.pedidos.domain.api;

import com.plazoleta.pedidos.domain.model.Pedido;

public interface CrearPedidoPort {
    Pedido crearPedido(Pedido pedido);
}
