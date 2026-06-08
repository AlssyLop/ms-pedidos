package com.plazoleta.pedidos.domain.spi;

import com.plazoleta.pedidos.domain.model.Trazabilidad;
import java.util.List;

public interface TrazabilidadRepositoryPort {
    List<Trazabilidad> findByIdPedidoOrderByFechaCambioAsc(Long idPedido);

    List<Trazabilidad> findByIdPedidoInOrderByFechaCambioAsc(List<Long> idPedidos);

    Trazabilidad save(Trazabilidad trazabilidad);
}