package com.plazoleta.pedidos.domain.api;

public interface NotificarPedidoListoPort {
    String notificar(Long idPedido, Long idEmpleado);
}
