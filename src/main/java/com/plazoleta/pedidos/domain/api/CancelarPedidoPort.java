package com.plazoleta.pedidos.domain.api;

import com.plazoleta.pedidos.domain.model.Pedido;

public interface CancelarPedidoPort {
    Pedido cancelarPedido(Long idPedido, Long idCliente);
}