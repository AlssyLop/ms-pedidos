package com.plazoleta.pedidos.domain.api;

import com.plazoleta.pedidos.domain.model.EstadoPedido;
import com.plazoleta.pedidos.domain.model.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ListarPedidosPorEstadoPort {
    Page<Pedido> listar(Long idRestaurante, EstadoPedido estado, Pageable pageable);
}