package com.plazoleta.pedidos.domain.usecase;

import com.plazoleta.pedidos.domain.api.ConsultarTrazabilidadPort;
import com.plazoleta.pedidos.domain.model.Pedido;
import com.plazoleta.pedidos.domain.model.Trazabilidad;
import com.plazoleta.pedidos.domain.spi.PedidoRepositoryPort;
import com.plazoleta.pedidos.domain.spi.TrazabilidadRepositoryPort;
import java.util.List;

public class ConsultarTrazabilidad implements ConsultarTrazabilidadPort {

    private final PedidoRepositoryPort pedidoRepository;
    private final TrazabilidadRepositoryPort trazabilidadRepository;

    public ConsultarTrazabilidad(PedidoRepositoryPort pedidoRepository,
                                  TrazabilidadRepositoryPort trazabilidadRepository) {
        this.pedidoRepository = pedidoRepository;
        this.trazabilidadRepository = trazabilidadRepository;
    }

    @Override
    public List<Trazabilidad> consultar(Long idPedido, Long idCliente) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new IllegalArgumentException("El pedido no existe"));

        if (!pedido.getIdCliente().equals(idCliente)) {
            throw new IllegalArgumentException("No tienes permiso para consultar este pedido");
        }

        return trazabilidadRepository.findByIdPedidoOrderByFechaCambioAsc(idPedido);
    }
}