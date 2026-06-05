package com.plazoleta.pedidos.domain.usecase;

import com.plazoleta.pedidos.domain.api.CancelarPedidoPort;
import com.plazoleta.pedidos.domain.model.EstadoPedido;
import com.plazoleta.pedidos.domain.model.Pedido;
import com.plazoleta.pedidos.domain.spi.PedidoRepositoryPort;
import java.util.Optional;

public class CancelarPedido implements CancelarPedidoPort {

    private final PedidoRepositoryPort pedidoRepository;

    public CancelarPedido(PedidoRepositoryPort pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public Pedido cancelarPedido(Long idPedido, Long idCliente) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new IllegalArgumentException("El pedido no existe"));

        if (!pedido.getIdCliente().equals(idCliente)) {
            throw new IllegalArgumentException("No tienes permiso para cancelar este pedido");
        }

        if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
            throw new IllegalArgumentException(
                    "Lo sentimos, tu pedido ya esta en preparacion y no puede cancelarse");
        }

        pedido.setEstado(EstadoPedido.CANCELADO);
        return pedidoRepository.save(pedido);
    }
}