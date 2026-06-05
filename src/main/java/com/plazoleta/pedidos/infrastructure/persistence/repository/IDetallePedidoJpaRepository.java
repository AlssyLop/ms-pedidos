package com.plazoleta.pedidos.infrastructure.persistence.repository;

import com.plazoleta.pedidos.infrastructure.entity.EntidadDetallePedido;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDetallePedidoJpaRepository extends JpaRepository<EntidadDetallePedido, Long> {

    List<EntidadDetallePedido> findByPedidoId(Long idPedido);
}
