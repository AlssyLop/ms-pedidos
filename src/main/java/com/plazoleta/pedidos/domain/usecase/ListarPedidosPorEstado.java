package com.plazoleta.pedidos.domain.usecase;

import com.plazoleta.pedidos.domain.api.ListarPedidosPorEstadoPort;
import com.plazoleta.pedidos.domain.model.EstadoPedido;
import com.plazoleta.pedidos.domain.model.Pedido;
import com.plazoleta.pedidos.domain.spi.PedidoRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ListarPedidosPorEstado implements ListarPedidosPorEstadoPort {

    private final PedidoRepositoryPort pedidoRepository;

    public ListarPedidosPorEstado(PedidoRepositoryPort pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public Page<Pedido> listar(Long idRestaurante, EstadoPedido estado, Pageable pageable) {
        if (estado != null) {
            return pedidoRepository.findByIdRestauranteAndEstado(idRestaurante, estado, pageable);
        }
        return pedidoRepository.findByIdRestaurante(idRestaurante, pageable);
    }
}