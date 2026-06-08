package com.plazoleta.pedidos.domain.usecase;

import com.plazoleta.pedidos.domain.api.CancelarPedidoPort;
import com.plazoleta.pedidos.domain.model.EstadoPedido;
import com.plazoleta.pedidos.domain.model.Pedido;
import com.plazoleta.pedidos.domain.model.Trazabilidad;
import com.plazoleta.pedidos.domain.spi.PedidoRepositoryPort;
import com.plazoleta.pedidos.domain.spi.TrazabilidadRepositoryPort;
import java.time.LocalDateTime;
import java.util.Optional;

public class CancelarPedido implements CancelarPedidoPort {

    private final PedidoRepositoryPort pedidoRepository;
    private final TrazabilidadRepositoryPort trazabilidadRepository;

    public CancelarPedido(PedidoRepositoryPort pedidoRepository,
                          TrazabilidadRepositoryPort trazabilidadRepository) {
        this.pedidoRepository = pedidoRepository;
        this.trazabilidadRepository = trazabilidadRepository;
    }

    @Override
    public Pedido cancelarPedido(Long idPedido, Long idCliente) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new IllegalArgumentException("El pedido no existe"));

        if (!pedido.getIdCliente().equals(idCliente)) {
            throw new IllegalArgumentException("No tienes permiso para cancelar este pedido");
        }

        if (pedido.getEstado() == EstadoPedido.CANCELADO) {
            throw new IllegalArgumentException("El pedido ya fue cancelado");
        }
        if (pedido.getEstado() == EstadoPedido.ENTREGADO) {
            throw new IllegalArgumentException("El pedido ya fue entregado, no puede cancelarse");
        }
        if (pedido.getEstado() == EstadoPedido.LISTO) {
            throw new IllegalArgumentException("Lo sentimos, tu pedido ya esta listo y no puede cancelarse");
        }
        if (pedido.getEstado() == EstadoPedido.EN_PREPARACION) {
            throw new IllegalArgumentException("Lo sentimos, tu pedido ya esta en preparacion y no puede cancelarse");
        }
        if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
            throw new IllegalArgumentException("Lo sentimos, tu pedido no puede cancelarse");
        }

        pedido.setEstado(EstadoPedido.CANCELADO);
        Pedido guardado = pedidoRepository.save(pedido);

        trazabilidadRepository.save(new Trazabilidad(
                null,
                guardado.getId(),
                guardado.getIdCliente(),
                guardado.getIdRestaurante(),
                EstadoPedido.PENDIENTE.name(),
                EstadoPedido.CANCELADO.name(),
                LocalDateTime.now(),
                null,
                null
        ));

        return guardado;
    }
}