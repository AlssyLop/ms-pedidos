package com.plazoleta.pedidos.infrastructure.persistence.adapter;

import com.plazoleta.pedidos.domain.model.EstadoPedido;
import com.plazoleta.pedidos.domain.model.Pedido;
import com.plazoleta.pedidos.domain.spi.PedidoRepositoryPort;
import com.plazoleta.pedidos.infrastructure.entity.EntidadDetallePedido;
import com.plazoleta.pedidos.infrastructure.entity.EntidadPedido;
import com.plazoleta.pedidos.infrastructure.entity.EstadoPedidoEntity;
import com.plazoleta.pedidos.infrastructure.persistence.mapper.IPedidoEntityMapper;
import com.plazoleta.pedidos.infrastructure.persistence.repository.IDetallePedidoJpaRepository;
import com.plazoleta.pedidos.infrastructure.persistence.repository.IPedidoJpaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class PedidoRepositoryAdapter implements PedidoRepositoryPort {

    private final IPedidoJpaRepository pedidoJpaRepository;
    private final IDetallePedidoJpaRepository detallePedidoJpaRepository;
    private final IPedidoEntityMapper mapper;

    public PedidoRepositoryAdapter(IPedidoJpaRepository pedidoJpaRepository,
                                   IDetallePedidoJpaRepository detallePedidoJpaRepository,
                                   IPedidoEntityMapper mapper) {
        this.pedidoJpaRepository = pedidoJpaRepository;
        this.detallePedidoJpaRepository = detallePedidoJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Pedido save(Pedido pedido) {
        EntidadPedido entity = mapper.toEntity(pedido);
        entity = pedidoJpaRepository.save(entity);

        if (pedido.getDetalles() != null) {
            for (var detalle : pedido.getDetalles()) {
                EntidadDetallePedido detEntity = mapper.detalleToEntity(detalle, entity);
                detallePedidoJpaRepository.save(detEntity);
            }
        }

        return findById(entity.getId()).orElseThrow();
    }

    @Override
    public Optional<Pedido> findById(Long id) {
        return pedidoJpaRepository.findById(id).map(entity -> {
            Pedido domain = mapper.toDomain(entity);
            List<EntidadDetallePedido> detEntities = detallePedidoJpaRepository.findByPedidoId(id);
            domain.setDetalles(mapper.detallesToDomain(detEntities));
            return domain;
        });
    }

    @Override
    public boolean existsByIdClienteAndEstadoIn(Long idCliente, List<EstadoPedido> estados) {
        List<EstadoPedidoEntity> estadosEntity = estados.stream()
                .map(e -> EstadoPedidoEntity.valueOf(e.name()))
                .toList();
        return pedidoJpaRepository.existsByIdClienteAndEstadoIn(idCliente, estadosEntity);
    }

    @Override
    public Page<Pedido> findByIdRestaurante(Long idRestaurante, Pageable pageable) {
        return pedidoJpaRepository.findByIdRestauranteOrderByFechaCreacionDesc(idRestaurante, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Page<Pedido> findByIdRestauranteAndEstado(Long idRestaurante, EstadoPedido estado, Pageable pageable) {
        return pedidoJpaRepository
                .findByIdRestauranteAndEstadoOrderByFechaCreacionDesc(idRestaurante, EstadoPedidoEntity.valueOf(estado.name()), pageable)
                .map(mapper::toDomain);
    }

    @Override
    public List<Pedido> findAllByIdRestauranteAndEstado(Long idRestaurante, EstadoPedido estado) {
        return pedidoJpaRepository
                .findByIdRestauranteAndEstadoOrderByFechaCreacionDesc(idRestaurante, EstadoPedidoEntity.valueOf(estado.name()))
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
