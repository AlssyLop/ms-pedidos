package com.plazoleta.pedidos.infrastructure.document;

import com.plazoleta.pedidos.domain.model.Trazabilidad;
import com.plazoleta.pedidos.domain.spi.TrazabilidadRepositoryPort;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TrazabilidadRepositoryAdapter implements TrazabilidadRepositoryPort {

    private final ITrazabilidadMongoRepository mongoRepository;

    public TrazabilidadRepositoryAdapter(ITrazabilidadMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public List<Trazabilidad> findByIdPedidoOrderByFechaCambioAsc(Long idPedido) {
        return mongoRepository.findByIdPedidoOrderByFechaCambioAsc(idPedido).stream()
                .map(e -> new Trazabilidad(
                        e.getId(),
                        e.getIdPedido(),
                        e.getEstadoAnterior(),
                        e.getEstadoNuevo(),
                        e.getFechaCambio(),
                        e.getDuracionEtapaMinutos()))
                .toList();
    }

    @Override
    public List<Trazabilidad> findByIdPedidoInOrderByFechaCambioAsc(List<Long> idPedidos) {
        return mongoRepository.findByIdPedidoInOrderByFechaCambioAsc(idPedidos).stream()
                .map(e -> new Trazabilidad(
                        e.getId(),
                        e.getIdPedido(),
                        e.getEstadoAnterior(),
                        e.getEstadoNuevo(),
                        e.getFechaCambio(),
                        e.getDuracionEtapaMinutos()))
                .toList();
    }
}