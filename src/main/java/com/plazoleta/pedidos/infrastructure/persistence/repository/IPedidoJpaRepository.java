package com.plazoleta.pedidos.infrastructure.persistence.repository;

import com.plazoleta.pedidos.infrastructure.entity.EntidadPedido;
import com.plazoleta.pedidos.infrastructure.entity.EstadoPedidoEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPedidoJpaRepository extends JpaRepository<EntidadPedido, Long> {

    boolean existsByIdClienteAndEstadoIn(Long idCliente, List<EstadoPedidoEntity> estados);

    Page<EntidadPedido> findByIdRestauranteOrderByFechaCreacionDesc(Long idRestaurante, Pageable pageable);

    Page<EntidadPedido> findByIdRestauranteAndEstadoOrderByFechaCreacionDesc(Long idRestaurante, EstadoPedidoEntity estado, Pageable pageable);

    List<EntidadPedido> findByIdRestauranteAndEstadoOrderByFechaCreacionDesc(Long idRestaurante, EstadoPedidoEntity estado);
}
