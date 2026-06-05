package com.plazoleta.pedidos.infrastructure.persistence.mapper;

import com.plazoleta.pedidos.domain.model.DetallePedido;
import com.plazoleta.pedidos.domain.model.EstadoPedido;
import com.plazoleta.pedidos.domain.model.Pedido;
import com.plazoleta.pedidos.infrastructure.entity.EntidadDetallePedido;
import com.plazoleta.pedidos.infrastructure.entity.EntidadPedido;
import com.plazoleta.pedidos.infrastructure.entity.EstadoPedidoEntity;
import java.util.Collections;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface IPedidoEntityMapper {

    @Mapping(target = "estado", source = "domain.estado", qualifiedByName = "estadoToEntity")
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaModificacion", ignore = true)
    EntidadPedido toEntity(Pedido domain);

    @Mapping(target = "estado", source = "entity.estado", qualifiedByName = "estadoToDomain")
    @Mapping(target = "detalles", ignore = true)
    Pedido toDomain(EntidadPedido entity);

    @Named("estadoToEntity")
    default EstadoPedidoEntity estadoToEntity(EstadoPedido estado) {
        if (estado == null) return null;
        return EstadoPedidoEntity.valueOf(estado.name());
    }

    @Named("estadoToDomain")
    default EstadoPedido estadoToDomain(EstadoPedidoEntity entity) {
        if (entity == null) return null;
        return EstadoPedido.valueOf(entity.name());
    }

    default List<DetallePedido> detallesToDomain(List<EntidadDetallePedido> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream()
                .map(e -> new DetallePedido(e.getId(), e.getPedido().getId(),
                        e.getIdPlato(), e.getNombrePlato(), e.getCantidad()))
                .toList();
    }

    default EntidadDetallePedido detalleToEntity(DetallePedido domain, EntidadPedido pedido) {
        if (domain == null) return null;
        EntidadDetallePedido entity = new EntidadDetallePedido();
        entity.setPedido(pedido);
        entity.setIdPlato(domain.getIdPlato());
        entity.setNombrePlato(domain.getNombrePlato());
        entity.setCantidad(domain.getCantidad());
        return entity;
    }
}
