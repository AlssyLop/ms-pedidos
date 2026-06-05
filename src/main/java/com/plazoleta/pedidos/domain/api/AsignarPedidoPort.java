package com.plazoleta.pedidos.domain.api;

import com.plazoleta.pedidos.domain.model.Pedido;

public interface AsignarPedidoPort {
    Pedido asignarPedido(Long idPedido, Long idEmpleado);
}