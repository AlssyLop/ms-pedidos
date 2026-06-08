package com.plazoleta.pedidos.infrastructure.document;

import com.plazoleta.pedidos.domain.model.Trazabilidad;
import com.plazoleta.pedidos.domain.spi.TrazabilidadRepositoryPort;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
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
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Trazabilidad> findByIdPedidoInOrderByFechaCambioAsc(List<Long> idPedidos) {
        return mongoRepository.findByIdPedidoInOrderByFechaCambioAsc(idPedidos).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Trazabilidad save(Trazabilidad trazabilidad) {
        Optional<EntidadTrazabilidad> ultimoRegistro = mongoRepository
                .findByIdPedidoOrderByFechaCambioAsc(trazabilidad.getIdPedido())
                .stream()
                .reduce((first, second) -> second);

        if (ultimoRegistro.isPresent()) {
            long minutos = ChronoUnit.MINUTES.between(
                    ultimoRegistro.get().getFechaCambio(),
                    trazabilidad.getFechaCambio());
            trazabilidad.setDuracionEtapaMinutos(Math.max(minutos, 0L));
        }

        EntidadTrazabilidad entity = toEntity(trazabilidad);
        entity = mongoRepository.save(entity);
        return toDomain(entity);
    }

    private EntidadTrazabilidad toEntity(Trazabilidad d) {
        EntidadTrazabilidad e = new EntidadTrazabilidad();
        e.setId(d.getId());
        e.setIdPedido(d.getIdPedido());
        e.setIdCliente(d.getIdCliente());
        e.setIdRestaurante(d.getIdRestaurante());
        e.setEstadoAnterior(d.getEstadoAnterior());
        e.setEstadoNuevo(d.getEstadoNuevo());
        e.setFechaCambio(d.getFechaCambio());
        e.setIdEmpleado(d.getIdEmpleado());
        e.setDuracionEtapaMinutos(d.getDuracionEtapaMinutos());
        return e;
    }

    private Trazabilidad toDomain(EntidadTrazabilidad e) {
        return new Trazabilidad(
                e.getId(),
                e.getIdPedido(),
                e.getIdCliente(),
                e.getIdRestaurante(),
                e.getEstadoAnterior(),
                e.getEstadoNuevo(),
                e.getFechaCambio(),
                e.getIdEmpleado(),
                e.getDuracionEtapaMinutos());
    }
}