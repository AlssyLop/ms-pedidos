package com.plazoleta.pedidos.domain.api;

import com.plazoleta.pedidos.domain.model.Pedido;

public interface EntregarPedidoPort {
    Pedido entregarPedido(Long idPedido, Long idEmpleado, String pin);
}